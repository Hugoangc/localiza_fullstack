package com.practice.localiza.service;

import com.practice.localiza.dto.PaymentRequestDTO;
import com.practice.localiza.entity.*;
import com.practice.localiza.enums.OrderStatus;
import com.practice.localiza.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService; //

    @Autowired
    private PaymentService paymentService;


    @Transactional
    public Order createOrder(User user, PaymentRequestDTO paymentDetails) {

        // pega o cart
        Cart cart = cartService.getCart(user);
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Seu carrinho está vazio.");
        }

        // Processando o Pagamento
        Payment payment = paymentService.processPayment(paymentDetails, cart.getTotalPrice());

        // Criando o pedido
        Order order = new Order();
        order.setUser(user);
        order.setPayment(payment);
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderDate(Instant.now());
        order.setStatus(OrderStatus.PAID);

        // 4. Converter CartItems em OrderItems
//        List<OrderItem> orderItems = new ArrayList<>();
//        for (CartItem cartItem : cart.getItems()) {
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order);
//            orderItem.setQuantity(1);
//
//            // copy car name
//            orderItem.setCarId(cartItem.getCar().getId());
//            orderItem.setCarName(cartItem.getCar().getName());
//            orderItem.setPurchasedPrice(cartItem.getCalculatedPrice());
//
//            // acc names
//            List<String> accessoryNames = cartItem.getCar().getAcessories()
//                    .stream()
//                    .map(Acessory::getName)
//                    .collect(Collectors.toList());
//            orderItem.setPurchasedAccessories(accessoryNames);
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(1);

            orderItem.setCarId(cartItem.getCar().getId());
            orderItem.setCarName(cartItem.getCar().getName());
            orderItem.setPurchasedPrice(cartItem.getCalculatedPrice());

            List<String> accessoryNames = cartItem.getChosenAccessories()
                    .stream()
                    .map(Acessory::getName)
                    .collect(Collectors.toList());
            orderItem.setPurchasedAccessories(accessoryNames);

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);

        // salvar o Order e os OrderItems por cascata
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(user);
        return savedOrder;
    }
}
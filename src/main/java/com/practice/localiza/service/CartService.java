package com.practice.localiza.service;

import com.practice.localiza.entity.*;
import com.practice.localiza.entity.User;
import com.practice.localiza.exception.ResourceNotFoundException;
import com.practice.localiza.repository.CarRepository;
import com.practice.localiza.repository.CartItemRepository;
import com.practice.localiza.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CarRepository carRepository;


    @Autowired
    private CartItemRepository cartItemRepository; //

    @Transactional
    public Cart getCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }


    @Transactional
    public CartItem addToCart(User User, Long carId) {
        Cart cart = getCart(User);

        Car carToAdd = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", carId));

        double finalPrice = carToAdd.getPrice();

        double multiplier = carToAdd.getAccMultiplier();
        for (Acessory acessory : carToAdd.getAcessories()) {
            finalPrice += (acessory.getPrice() * multiplier);
        }
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setCar(carToAdd);
        cartItem.setCalculatedPrice(finalPrice);

        cart.getItems().add(cartItem);
        updateCartTotal(cart);
        cartRepository.save(cart);
        return cartItem;
    }
    @Transactional
    public void clearCart(User user) { // <-- Mude de (Long userId) para (User user)
        // Agora esta linha funciona perfeitamente, pois o 'user' foi recebido
        Cart cart = getCart(user);

        if (cart != null && cart.getItems() != null && !cart.getItems().isEmpty()) {
            // Deletar os itens do repositório
            cartItemRepository.deleteAll(cart.getItems());

            // Limpar a lista na entidade Cart
            cart.getItems().clear();

            // Atualizar o total (deve ir para 0)
            updateCartTotal(cart);

            // Salvar o carrinho agora vazio
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void removeFromCart(User user, Long cartItemId) {
        Cart cart = getCart(user);
        cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        updateCartTotal(cart);
        cartRepository.save(cart);
    }

    private void updateCartTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(CartItem::getCalculatedPrice)
                .sum();
        cart.setTotalPrice(total);
    }
}
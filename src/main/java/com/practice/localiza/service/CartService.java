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
import com.practice.localiza.dto.CartItemRequestDTO;
import com.practice.localiza.repository.AcessoryRepository;
import com.practice.localiza.repository.CartItemRepository;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private AcessoryRepository acessoryRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

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
    public CartItem addToCart(User user, CartItemRequestDTO request) {
        Cart cart = getCart(user);

        Car carToAdd = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car", request.getCarId()));

        List<Acessory> chosenAccessories = acessoryRepository.findAllById(request.getAccessoryIds());

        double finalPrice = calculatePrice(carToAdd, chosenAccessories);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setCar(carToAdd);
        cartItem.setChosenAccessories(chosenAccessories);
        cartItem.setCalculatedPrice(finalPrice);

        cart.getItems().add(cartItem);
        updateCartTotal(cart);
        cartRepository.save(cart);
        return cartItem;
    }
    @Transactional
    public void clearCart(User user) {
        Cart cart = getCart(user);
        if (cart != null && cart.getItems() != null && !cart.getItems().isEmpty()) {
            cartItemRepository.deleteAll(cart.getItems());
            cart.getItems().clear();
            updateCartTotal(cart);
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void removeFromCart(User user, Long cartItemId) {
        Cart cart = getCart(user);

        cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        cartItemRepository.deleteById(cartItemId);

        updateCartTotal(cart);
        cartRepository.save(cart);
    }

    @Transactional
    public CartItem updateCartItemAccessories(User user, Long cartItemId, CartItemRequestDTO request) {
        Cart cart = getCart(user);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new SecurityException("Este item não pertence ao seu carrinho.");
        }

        List<Acessory> newAccessories = acessoryRepository.findAllById(request.getAccessoryIds());
        Car car = cartItem.getCar();
        double finalPrice = calculatePrice(car, newAccessories);
        cartItem.setChosenAccessories(newAccessories);
        cartItem.setCalculatedPrice(finalPrice);
        cartItemRepository.save(cartItem);
        updateCartTotal(cart);
        cartRepository.save(cart);

        return cartItem;
    }
    private void updateCartTotal(Cart cart) {
        Double total = cart.getItems().stream()
                .mapToDouble(CartItem::getCalculatedPrice)
                .sum();
        cart.setTotalPrice(total);
    }

    private Double calculatePrice(Car car, List<Acessory> chosenAccessories) {
        double finalPrice = car.getPrice();
        double multiplier = car.getAccMultiplier(); //

        for (Acessory acessory : chosenAccessories) {
            finalPrice += (acessory.getPrice() * multiplier);
        }
        return finalPrice;
    }

}
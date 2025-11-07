package com.practice.localiza.controller;
import com.practice.localiza.dto.CartItemRequestDTO;
import com.practice.localiza.entity.Cart;
import com.practice.localiza.entity.CartItem;
import com.practice.localiza.entity.User;
import com.practice.localiza.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getMyCart(@AuthenticationPrincipal User user) {
        Cart cart = cartService.getCart(user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(
            @AuthenticationPrincipal User user,
            @RequestBody CartItemRequestDTO request) {

        CartItem item = cartService.addToCart(user, request);
        return ResponseEntity.ok(item);
    }
    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @RequestBody CartItemRequestDTO request) {

        CartItem updatedItem = cartService.updateCartItemAccessories(user, cartItemId, request);
        return ResponseEntity.ok(updatedItem);
    }


    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId) {

        cartService.removeFromCart(user, cartItemId);
        return ResponseEntity.noContent().build();
    }
}
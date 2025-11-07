package com.practice.localiza.controller;

import com.practice.localiza.dto.PaymentRequestDTO;
import com.practice.localiza.entity.Order;
import com.practice.localiza.entity.User;
import com.practice.localiza.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(
            @AuthenticationPrincipal User user,
            @RequestBody PaymentRequestDTO paymentDetails) {

        try {
            Order order = orderService.createOrder(user, paymentDetails);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            //  TODO: usar seu GlobalExceptionHandler aqui
            return ResponseEntity.badRequest().body(null);
        }
    }

}
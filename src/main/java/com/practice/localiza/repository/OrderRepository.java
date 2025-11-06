package com.practice.localiza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.practice.localiza.entity.Order;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<OrderRepository> findByUserId(Long userId);
}

// Em PaymentRepository.java
package com.practice.localiza.repository;

import com.practice.localiza.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

// 3. Adicione "extends JpaRepository<Payment, Long>"
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment save(Payment payment);
}








package com.practice.localiza.service;

import com.practice.localiza.dto.PaymentRequestDTO;
import com.practice.localiza.entity.Payment;
import com.practice.localiza.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository; //


    public Payment processPayment(PaymentRequestDTO paymentDetails, Double amount) {

        // boolean paymentSuccess = gateway.charge(paymentDetails, amount);

        boolean paymentSuccess = true;

        if (paymentSuccess) {
            Payment payment = new Payment();
            payment.setMoment(Instant.now());
            // payment.setTransactionId("xyz123");

            if (paymentDetails.getSaveCard()) {
            }

            return paymentRepository.save(payment);
        } else {
            throw new RuntimeException("Pagamento falhou. Verifique os dados do cartão.");
        }
    }
}
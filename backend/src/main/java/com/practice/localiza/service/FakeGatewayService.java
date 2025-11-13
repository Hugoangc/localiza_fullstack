package com.practice.localiza.service;

import com.practice.localiza.dto.ChargeResult;
import com.practice.localiza.dto.PaymentRequestDTO;
import com.practice.localiza.entity.User;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class FakeGatewayService {


    public ChargeResult executeCharge(PaymentRequestDTO paymentDetails, Double amount) {


        String cardNumber = paymentDetails.getCardNumber();

        if (cardNumber == null || cardNumber.length() < 16) {
            return ChargeResult.failure("Formato de cartão inválido.");
        }

        String lastFour = cardNumber.substring(cardNumber.length() - 4);



        // Cartão terminado em "0000" é sempre aprovado
        if (lastFour.equals("0000")) {
            String fakeTransactionId = "ch_sim_" + Instant.now().toEpochMilli();
            return ChargeResult.success(fakeTransactionId);
        }

        //  Cartão terminado em "1111" tem limite baixo
        if (lastFour.equals("1111")) {
            if (amount > 1000.0) {
                return ChargeResult.failure("Fundos Insuficientes (Cartão de Teste com Limite)");
            }
            String fakeTransactionId = "ch_sim_" + Instant.now().toEpochMilli();
            return ChargeResult.success(fakeTransactionId);
        }

        return ChargeResult.failure("Cartão Recusado (Cartão de Teste Inválido)");
    }


    public String createGatewayCustomer(User user) {
        String fakeCustomerId = "cus_sim_" + user.getId() + "_" + Instant.now().toEpochMilli();
        System.out.println("LOG: [FakeGateway] Criando cliente " + fakeCustomerId + " para o usuário " + user.getUsername());
        return fakeCustomerId;
    }


    public void updateGatewayCustomer(String customerId, PaymentRequestDTO paymentDetails) {
        System.out.println("LOG: [FakeGateway] Atualizando cliente " + customerId + " com o cartão final " + paymentDetails.getCardNumber().substring(12));
    }
}
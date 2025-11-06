package com.practice.localiza.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    private Boolean saveCard;

}
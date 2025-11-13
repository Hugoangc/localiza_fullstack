package com.practice.localiza.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargeResult {


    //DTO PARA SIMULAR UM GATEWAY DE PAGAMENTO
    private boolean successful;
    private String transactionId;
    private String failureMessage;

    private ChargeResult(boolean successful, String transactionId, String failureMessage) {
        this.successful = successful;
        this.transactionId = transactionId;
        this.failureMessage = failureMessage;
    }

    // Mét odo para criar uma resposta de sucesso
    public static ChargeResult success(String transactionId) {
        return new ChargeResult(true, transactionId, null);
    }

    // Mét odo para criar uma resposta de falha
    public static ChargeResult failure(String failureMessage) {
        return new ChargeResult(false, null, failureMessage);
    }
}
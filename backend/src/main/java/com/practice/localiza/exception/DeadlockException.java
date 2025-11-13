package com.practice.localiza.exception;

public class DeadlockException extends RuntimeException {
    public DeadlockException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.bankSpark.analyticsService.exception;

public class GenerateKeyException extends RuntimeException {

    public GenerateKeyException() {
        super("Failed to obtain generation key!");
    }

}
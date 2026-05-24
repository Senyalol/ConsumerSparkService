package com.bankSpark.analyticsService.exception;

public class CreateAnalystException extends RuntimeException{

    public CreateAnalystException() {
        super("Failed to create analyst");
    }

}
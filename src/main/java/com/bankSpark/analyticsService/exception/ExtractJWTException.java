package com.bankSpark.analyticsService.exception;

public class ExtractJWTException extends RuntimeException{

    public ExtractJWTException() {
        super("Error extracting JWT form request");
    }

}
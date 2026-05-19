package com.bankSpark.analyticsService.exception;

public class AnomalyException extends RuntimeException{

    public AnomalyException() {
        super("Anomaly update failed , incorrect transaction or message");
    }

}
package com.bankSpark.analyticsService.exception;

public class UpdateAnalystException extends RuntimeException {

    public UpdateAnalystException() {
        super("Failed to update analytics");
    }

}
package com.bankSpark.analyticsService.exception;

public class KafkaCustomException extends RuntimeException {

    private String topic;
    private Object value;

    public KafkaCustomException(String topic, Object value) {
        String message = "KafkaException in topic: " + topic + " value: " + value;
        super(message);
    }

}
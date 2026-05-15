package com.bankSpark.analyticsService.exception;

public class KafkaCustomException extends RuntimeException {

    private String topic;
    private Object value;

//    public KafkaCustomException(String topic, Object value) {
//        String message = "KafkaException in topic: " + topic + " value: " + value;
//        super(message);
//    }

    public KafkaCustomException(String topic, Object value) {
        super(String.format("Kafka error in topic: %s, value: %s", topic, value));
        this.topic = topic;
        this.value = value;
    }

    public KafkaCustomException(String topic, Object value, Throwable cause) {
        super(String.format("Kafka error in topic: %s, value: %s", topic, value), cause);
        this.topic = topic;
        this.value = value;
    }


}
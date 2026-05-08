package com.bankSpark.analyticsService.DTO.anomaly;

import lombok.Data;

@Data
public class KafkaAnomalyDTO {

    private int user_id;
    private long event_time;
    private String type;
    private double sum;
    private double avg_check_5min;
    private String message;

}
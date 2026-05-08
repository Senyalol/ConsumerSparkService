package com.bankSpark.analyticsService.DTO.anomaly;

import lombok.Data;

@Data
public class AnomalyDTO {

    private Integer anomalyId;

    private Integer userId;

    private Long eventTime;

    private String type;

    private Double sum;
    
    private Double avgCheck;

    private String message;

}
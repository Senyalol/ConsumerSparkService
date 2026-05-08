package com.bankSpark.analyticsService.DTO.segmentsRFM;

import lombok.Data;

@Data
public class KafkaSegmentUserDTO {

    private int user_id;
    private String segment;
    private double r_minutes;
    private long f;
    private double m;
    private long updated_at;

}
package com.bankSpark.analyticsService.DTO;

import lombok.Data;

@Data
public class SegmentuserDTO {

    private Integer uSegmentId;

    private Integer userId;

    private String segment;

    private Double rMinutes;

    private Long f;

    private Double m;

    private Long updatedAt;

}
package com.bankSpark.analyticsService.DTO.segmentsRFM;

import lombok.Data;

@Data
public class SegmentUserDTO {

    private Integer uSegmentId;

    private Integer userId;

    private String segment;

    private String rMinutes;  // ← был Double, стал String
//    private Double rMinutes;

    private Long f;

    private Double m;

    private String updatedAt;


}
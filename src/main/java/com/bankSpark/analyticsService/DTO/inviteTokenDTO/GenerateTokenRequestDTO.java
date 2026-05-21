package com.bankSpark.analyticsService.DTO.inviteTokenDTO;

import lombok.Data;

@Data
public class GenerateTokenRequestDTO {

    private String role;

    private Integer hoursValid;

}
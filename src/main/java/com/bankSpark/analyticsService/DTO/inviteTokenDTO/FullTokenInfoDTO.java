package com.bankSpark.analyticsService.DTO.inviteTokenDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FullTokenInfoDTO {

    private String token;
    private String role;
    private Boolean used;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

}
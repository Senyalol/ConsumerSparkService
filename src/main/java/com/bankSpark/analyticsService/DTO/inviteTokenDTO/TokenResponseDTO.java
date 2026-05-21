package com.bankSpark.analyticsService.DTO.inviteTokenDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenResponseDTO {

    private String token;
    private String role;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public TokenResponseDTO() {}

    public TokenResponseDTO(String token, String role, LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.token = token;
        this.role = role;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

}
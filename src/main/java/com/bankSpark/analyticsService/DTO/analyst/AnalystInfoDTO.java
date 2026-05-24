package com.bankSpark.analyticsService.DTO.analyst;

import lombok.Data;
import java.time.Instant;

@Data
public class AnalystInfoDTO {

    private String token;

    private String login;

    private String password;

    private String role;

    private Instant createdAt;

}
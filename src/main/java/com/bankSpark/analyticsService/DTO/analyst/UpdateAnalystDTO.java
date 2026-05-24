package com.bankSpark.analyticsService.DTO.analyst;

import lombok.Data;

@Data
public class UpdateAnalystDTO {

    private String token;

    private String login;

    private String password;

    private String role;

}
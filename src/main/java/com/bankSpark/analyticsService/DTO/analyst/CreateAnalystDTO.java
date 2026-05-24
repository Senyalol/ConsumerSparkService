package com.bankSpark.analyticsService.DTO.analyst;

import lombok.Data;

@Data
public class CreateAnalystDTO {

    private String token;

    private String login;

    private String password;

}
package com.bankSpark.analyticsService.security.sDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtTokenDTO {

    @JsonProperty("token")
    private String token;

}
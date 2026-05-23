package com.bankSpark.analyticsService.security;

import com.bankSpark.analyticsService.security.sDTO.JwtAuthenticationDTO;

public interface JWTService{

    boolean validateJwtToken(String token);

    String generateJwtToken(String login, String role, String password);

    JwtAuthenticationDTO getTokenForUser(String login);

    JwtAuthenticationDTO generateRefreshToken(String login, String refreshToken);

    String getLoginFromToken(String token);

    JwtAuthenticationDTO getOutFromAccount(String login);

}
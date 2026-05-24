package com.bankSpark.analyticsService.security;

import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.DTO.analyst.AuthAnalystDTO;
import com.bankSpark.analyticsService.security.sDTO.JwtAuthenticationDTO;
import com.bankSpark.analyticsService.security.sDTO.JwtTokenDTO;

public interface JWTService{

    boolean validateJwtToken(String token);

    String generateJwtToken(String login, String role, String password,String token);

    JwtAuthenticationDTO getTokenForUser(String login);

    JwtAuthenticationDTO generateRefreshToken(String login, String refreshToken);

    String getLoginFromToken(String token);

    JwtAuthenticationDTO getOutFromAccount(String login);

    //Войти в аккаунт
    JwtAuthenticationDTO signIn(AuthAnalystDTO authDTO);

    //Выйти из аккаунта
    JwtTokenDTO getOut(String token);

    //Достать аналитика из токена
    Analyst analystFromToken(String jwt);

}
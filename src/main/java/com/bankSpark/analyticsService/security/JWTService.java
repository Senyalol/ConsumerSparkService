package com.bankSpark.analyticsService.security;

import com.bankSpark.analyticsService.security.sDTO.JwtAuthenticationDTO;

public interface JWTService{

    boolean validateJwtToken(String token);

    String generateJwtToken(String login, String role, String password,String token);

    JwtAuthenticationDTO getTokenForAnalyst(String login);

    JwtAuthenticationDTO generateRefreshToken(String login, String refreshToken);

    String getLoginFromToken(String token);

    JwtAuthenticationDTO getOutFromAccount(String login);

    String getRoleAnalyst(String login);

    String getTokenAnalyst(String login);

//    //Войти в аккаунт
//    JwtAuthenticationDTO signIn(AuthAnalystDTO authDTO);
//
//    //Выйти из аккаунта
//    JwtTokenDTO getOut(String token);
//
//    //Достать аналитика из токена
//    Analyst analystFromToken(String jwt);

}
package com.bankSpark.analyticsService.facade.analyst;

import com.bankSpark.analyticsService.DTO.analyst.AnalystInfoDTO;
import com.bankSpark.analyticsService.DTO.analyst.AuthAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.security.sDTO.JwtAuthenticationDTO;
import com.bankSpark.analyticsService.security.sDTO.JwtTokenDTO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface AnalystFacade {

    //Получить всех аналитиков
    List<AnalystInfoDTO> getAnalysts();

    //Получить аналитиков по роле
    List<AnalystInfoDTO> getAnalystsByRole(String role);

    //Получить аналитиков созданных после определенной даты
    List<AnalystInfoDTO> getAnalystsAfterCreatedAt(LocalDateTime created_at);

    //Получить аналитиков созданных до определенной даты
    List<AnalystInfoDTO> getAnalystsBeforeCreatedAt(LocalDateTime created_at);

    //Получить аналитика по id
    AnalystInfoDTO getAnalystById(int id);

    //Получить аналитика по логину
    AnalystInfoDTO getAnalystByLogin(String login);

    AnalystInfoDTO createAnalyst(CreateAnalystDTO createAnalystDTO);

    AnalystInfoDTO updateAnalyst(int id, UpdateAnalystDTO updateAnalystDTO);

    void deleteAnalyst(int id);

    //Войти в аккаунт
    JwtAuthenticationDTO signIn(AuthAnalystDTO authDTO);

    //Выйти из аккаунта
    JwtTokenDTO getOut(String token);

    //Достать аналитика из токена
    AnalystInfoDTO analystFromToken(String jwt);

}
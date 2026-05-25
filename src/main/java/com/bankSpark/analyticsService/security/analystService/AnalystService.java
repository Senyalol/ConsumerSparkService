package com.bankSpark.analyticsService.security.analystService;

import com.bankSpark.analyticsService.DTO.analyst.AnalystInfoDTO;
import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.security.sDTO.JwtTokenDTO;
import com.bankSpark.analyticsService.security.sDTO.JwtAuthenticationDTO;
import com.bankSpark.analyticsService.DTO.analyst.AuthAnalystDTO;

import java.time.Instant;
import java.util.List;

public interface AnalystService {

    //Получить всех аналитиков
    List<AnalystInfoDTO> getAnalysts();

    //Получить аналитиков по роле
    List<AnalystInfoDTO> getAnalystsByRole(String role);

    //Получить аналитиков созданных после определенной даты
    List<AnalystInfoDTO> getAnalystsAfterCreatedAt(Instant created_at);

    //Получить аналитиков созданных до определенной даты
    List<AnalystInfoDTO> getAnalystsBeforeCreatedAt(Instant created_at);

    //Получить аналитика по id
    AnalystInfoDTO getAnalystById(int id);

    //Получить аналитика по логину
    AnalystInfoDTO getAnalystByLogin(String login);

    AnalystInfoDTO createAnalyst(CreateAnalystDTO createAnalystDTO);

    AnalystInfoDTO updateAnalyst(int id, UpdateAnalystDTO updateAnalystDTO);

    void deleteAnalyst(int id);

    JwtAuthenticationDTO signIn(AuthAnalystDTO authDTO);
//
//    //Выйти из аккаунта
    JwtTokenDTO getOut(String token);
//
//    //Достать аналитика из токена
    Analyst analystFromToken(String jwt);

}
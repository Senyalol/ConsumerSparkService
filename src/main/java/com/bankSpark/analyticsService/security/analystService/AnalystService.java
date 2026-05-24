package com.bankSpark.analyticsService.security.analystService;

import com.bankSpark.analyticsService.DTO.analyst.AnalystInfoDTO;
import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;

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

}
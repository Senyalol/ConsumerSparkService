package com.bankSpark.analyticsService.facade.anomaly;

import com.bankSpark.analyticsService.DTO.anomaly.AnomalyDTO;

import java.util.List;

public interface AnomalyFacade {

    List<AnomalyDTO> getAllAnomalies();

    AnomalyDTO getAnomalyById(int id);

    List<AnomalyDTO> getAnomalyByType(String type);

    //Диапазон для суммы
    List<AnomalyDTO> getAnomaliesBySumRange(Double min, Double max);

    //Больше определенного чека
    List<AnomalyDTO> getAnomaliesByMoreSum(Double sum);

    //Меньше определенного чека
    List<AnomalyDTO> getAnomaliesByLessSum(Double sum);

    //Сортировки пользователей по активностям пользователей
    //Когда в каком промежутке были аномалии
    List<AnomalyDTO> getAnomaliesByEventTimeRange(Long minTime, Long maxTime);

    //Все аномалии до определенного времени
    List<AnomalyDTO> getAnomaliesByMinEventTime(Long minTime);

    //Все аномалии после определенного времени
    List<AnomalyDTO> getAnomaliesByMaxEventTime(Long maxTime);

    List<AnomalyDTO> getAnomaliesByAvgCheckLess(Double avgCheckLess);

    List<AnomalyDTO> getAnomaliesByAvgCheck(Double avgCheck);

    List<AnomalyDTO> getAnomaliesByAvgCheck(Double min, Double max);

    List<AnomalyDTO> getAnomaliesByUserId(int userId);

}
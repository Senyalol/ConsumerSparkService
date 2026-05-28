package com.bankSpark.analyticsService.service.anomaly;

import com.bankSpark.analyticsService.ORM.anomaly.Anomaly;

import java.util.List;

public interface AnomalyService {

    List<Anomaly> getAllAnomalies();

    Anomaly getAnomalyById(int id);

    List<Anomaly> getAnomalyByType(String type);

    //Диапазон для суммы
    List<Anomaly> getAnomaliesBySumRange(Double min, Double max);

    //Больше определенного чека
    List<Anomaly> getAnomaliesByMoreSum(Double sum);

    //Меньше определенного чека
    List<Anomaly> getAnomaliesByLessSum(Double sum);

    //Сортировки пользователей по активностям пользователей
    //Когда в каком промежутке были аномалии
    List<Anomaly> getAnomaliesByEventTimeRange(Long minTime, Long maxTime);

    //Все аномалии до определенного времени
    List<Anomaly> getAnomaliesByMinEventTime(Long minTime);

    List<Anomaly> getAnomaliesByAvgCheckLess(Double avgCheckLess);

    //Все аномалии после определенного времени
    List<Anomaly> getAnomaliesByMaxEventTime(Long maxTime);

    List<Anomaly> getAnomaliesByAvgCheck(Double avgCheck);

    List<Anomaly> getAnomaliesByAvgCheck(Double min, Double max);

    List<Anomaly> getAnomaliesByUserId(int userId);

}
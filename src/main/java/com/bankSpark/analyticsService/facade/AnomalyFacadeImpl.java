package com.bankSpark.analyticsService.facade;

import com.bankSpark.analyticsService.DTO.AnomalyDTO;
import com.bankSpark.analyticsService.service.anomaly.AnomalyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnomalyFacadeImpl implements AnomalyFacade {

    private final AnomalyService anomalyService;

    @Autowired
    public AnomalyFacadeImpl(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    @Override
    public List<AnomalyDTO> getAllAnomalies() {
        return List.of();
    }

    @Override
    public AnomalyDTO getAnomalyById(int id) {
        return null;
    }

    @Override
    public List<AnomalyDTO> getAnomalyByType(String type) {
        return List.of();
    }

    @Override
    public List<AnomalyDTO> getAnomaliesBySumRange(Double min, Double max) {
        return List.of();
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByMoreSum(Double sum) {
        return List.of();
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByLessSum(Double sum) {
        return List.of();
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByEventTimeRange(Long minTime, Long maxTime) {
        return List.of();
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByMinEventTime(Long minTime) {
        return List.of();
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByMaxEventTime(Long maxTime) {
        return List.of();
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByAvgCheck(Double avgCheck) {
        return List.of();
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByAvgCheck(Double min, Double max) {
        return List.of();
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByUserId(int userId) {
        return List.of();
    }

}
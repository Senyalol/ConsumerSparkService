package com.bankSpark.analyticsService.facade.anomaly;

import com.bankSpark.analyticsService.DTO.AnomalyDTO;
import com.bankSpark.analyticsService.mapper.AnomalyMapper;
import com.bankSpark.analyticsService.service.anomaly.AnomalyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnomalyFacadeImpl implements AnomalyFacade {

    private final AnomalyService anomalyService;
    private final AnomalyMapper anomalyMapper;

    @Autowired
    public AnomalyFacadeImpl(AnomalyService anomalyService, AnomalyMapper anomalyMapper) {
        this.anomalyService = anomalyService;
        this.anomalyMapper = anomalyMapper;
    }

    @Override
    public List<AnomalyDTO> getAllAnomalies() {
        return anomalyMapper.toListDTO(anomalyService.getAllAnomalies());
    }

    @Override
    public AnomalyDTO getAnomalyById(int id) {
        return anomalyMapper.toDTO(anomalyService.getAnomalyById(id));
    }

    @Override
    public List<AnomalyDTO> getAnomalyByType(String type) {
        return anomalyMapper.toListDTO(anomalyService.getAnomalyByType(type));
    }

    @Override
    public List<AnomalyDTO> getAnomaliesBySumRange(Double min, Double max) {
        return anomalyMapper.toListDTO(anomalyService.getAnomaliesBySumRange(min, max));
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByMoreSum(Double sum) {
        return anomalyMapper.toListDTO(anomalyService.getAnomaliesByMoreSum(sum));
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByLessSum(Double sum) {
        return anomalyMapper.toListDTO(anomalyService.getAnomaliesByLessSum(sum));
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByEventTimeRange(Long minTime, Long maxTime) {
        return anomalyMapper.toListDTO(anomalyService.getAnomaliesByEventTimeRange(minTime, maxTime));
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByMinEventTime(Long minTime) {
        return anomalyMapper.toListDTO(anomalyService.getAnomaliesByMinEventTime(minTime));
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByMaxEventTime(Long maxTime) {
        return anomalyMapper.toListDTO(anomalyService.getAnomaliesByMaxEventTime(maxTime));
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByAvgCheck(Double avgCheck) {
        return anomalyMapper.toListDTO(anomalyService.getAnomaliesByAvgCheck(avgCheck));
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByAvgCheck(Double min, Double max) {
        return anomalyMapper.toListDTO(anomalyService.getAnomaliesByAvgCheck(min, max));
    }

    @Override
    public List<AnomalyDTO> getAnomaliesByUserId(int userId) {
        return anomalyMapper.toListDTO(anomalyService.getAnomaliesByUserId(userId));
    }

}
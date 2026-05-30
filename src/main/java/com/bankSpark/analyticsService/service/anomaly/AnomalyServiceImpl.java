package com.bankSpark.analyticsService.service.anomaly;

import com.bankSpark.analyticsService.ORM.anomaly.Anomaly;
import com.bankSpark.analyticsService.ORM.anomaly.AnomalyType;
import com.bankSpark.analyticsService.repository.AnomalyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//@JsonSerialize
@Service
public class AnomalyServiceImpl implements AnomalyService {

    private final AnomalyRepository anomalyRepository;

    @Autowired
    public AnomalyServiceImpl(AnomalyRepository anomalyRepository) {
        this.anomalyRepository = anomalyRepository;
    }

    @Override
    public List<Anomaly> getAllAnomalies() {
        return anomalyRepository.findAll();
    }

    @Override
    public Anomaly getAnomalyById(int id) {
        return anomalyRepository.findById(id).get();
    }

    @Override
    public List<Anomaly> getAnomalyByType(String type) {

        boolean existsType = Arrays.stream(AnomalyType.values())
                .anyMatch(enumType -> enumType.name().equals(type));

            return existsType ? anomalyRepository.findByMessage(type) : Collections.emptyList();
    }

    @Override
    public List<Anomaly> getAnomaliesBySumRange(Double min, Double max) {
        return anomalyRepository.findAll().stream()
                .filter(x -> x.getSum() >= min && x.getSum() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anomaly> getAnomaliesByMoreSum(Double sum) {
        return anomalyRepository.findAll().stream()
                .filter(x -> x.getSum() >= sum)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anomaly> getAnomaliesByLessSum(Double sum) {
        return anomalyRepository.findAll().stream()
                .filter(x -> x.getSum() < sum)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anomaly> getAnomaliesByMinEventTime(Long minEventTime) {
        return anomalyRepository.findAll().stream()
                .filter(x -> x.getEventTime() <= minEventTime)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anomaly> getAnomaliesByMaxEventTime(Long maxEventTime) {
        return anomalyRepository.findAll().stream()
                .filter(x -> x.getEventTime() > maxEventTime)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anomaly> getAnomaliesByEventTimeRange(Long minTime, Long maxTime) {
        return anomalyRepository.findAll().stream()
                .filter(x -> x.getEventTime() >= minTime && x.getEventTime() <= maxTime)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anomaly> getAnomaliesByAvgCheck(Double avgCheck) {
        return anomalyRepository.findAll().stream()
                .filter(x -> x.getAvgCheck() >= avgCheck)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anomaly> getAnomaliesByAvgCheck(Double min, Double max) {
        return anomalyRepository.findAll().stream()
                .filter(x -> x.getAvgCheck() >= min && x.getAvgCheck() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anomaly> getAnomaliesByAvgCheckLess(Double avgCheckLess) {
        return anomalyRepository.findAll().stream()
                .filter(x -> x.getAvgCheck() <= avgCheckLess)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anomaly> getAnomaliesByUserId(int userId) {
        return anomalyRepository.findByUserId(userId);
    }

}
package com.bankSpark.analyticsService.service.anomaly;

import com.bankSpark.analyticsService.ORM.Anomaly;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnomalyService {

    List<Anomaly> getAllAnomalies();

    Anomaly getAnomalyById(int id);



}
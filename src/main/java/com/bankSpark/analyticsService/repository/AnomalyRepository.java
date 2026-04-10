package com.bankSpark.analyticsService.repository;

import com.bankSpark.analyticsService.ORM.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly,Integer> {

    Optional<Anomaly> findById(int id);
    Optional<Anomaly> findByUserId(int id);
    List<Anomaly> findByType(String type);
    List<Anomaly> findByEventTime(Long eventTime);

}
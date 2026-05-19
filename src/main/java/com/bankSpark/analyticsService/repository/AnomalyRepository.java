package com.bankSpark.analyticsService.repository;

import com.bankSpark.analyticsService.ORM.anomaly.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly,Integer> {

    Optional<Anomaly> findById(int id);
    List<Anomaly> findByUserId(int id);
    List<Anomaly> findByType(String type);
    List<Anomaly> findByEventTime(Long eventTime);

    int countAnomaliesByUserId(int id);

    //Разобраться
    @Query("SELECT a FROM Anomaly a WHERE a.user.id = :userId ORDER BY a.eventTime ASC LIMIT 1")
    Optional<Anomaly> findOldestAnomalyByUserId(int userId);

}
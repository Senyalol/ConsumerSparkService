package com.bankSpark.analyticsService.repository;

import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AnalystRepository extends JpaRepository<Analyst,Integer> {

    Optional<Analyst> getAnalystById(int id);
    Optional<Analyst> getAnalystByLogin(String login);
    List<Analyst> getAnalystsByRole(String role);
    List<Analyst> getAnalystsByCreatedAt(Instant createdAt);

}

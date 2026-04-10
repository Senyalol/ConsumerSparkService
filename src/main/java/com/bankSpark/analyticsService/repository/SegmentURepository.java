package com.bankSpark.analyticsService.repository;

import com.bankSpark.analyticsService.ORM.Segmentuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SegmentURepository extends JpaRepository<Segmentuser,Integer> {

    Optional<Segmentuser> findById(int id);
    Optional<Segmentuser> findByUserId(int userId);
    List<Segmentuser> findBySegment(String segment);

}
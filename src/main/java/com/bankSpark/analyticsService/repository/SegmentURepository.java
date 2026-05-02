package com.bankSpark.analyticsService.repository;

import com.bankSpark.analyticsService.ORM.SegmentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SegmentURepository extends JpaRepository<SegmentUser,Integer> {

    Optional<SegmentUser> findById(int id);
    Optional<SegmentUser> findByUserId(int userId);
    List<SegmentUser> findBySegment(String segment);

}
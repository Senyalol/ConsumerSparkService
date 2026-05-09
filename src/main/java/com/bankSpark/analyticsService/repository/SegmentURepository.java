package com.bankSpark.analyticsService.repository;

import com.bankSpark.analyticsService.ORM.SegmentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface SegmentURepository extends JpaRepository<SegmentUser,Integer> {

    Optional<SegmentUser> findById(int id);
    Optional<SegmentUser> findByUserId(int userId);
    List<SegmentUser> findBySegment(String segment);
    int countByUserId(int userId);

    @Query("SELECT s FROM SegmentUser s WHERE s.user.id = :userId ORDER BY s.rMinutes DESC LIMIT 1")
    Optional<SegmentUser> findOldestByUserId(@Param("userId") int userId);

}
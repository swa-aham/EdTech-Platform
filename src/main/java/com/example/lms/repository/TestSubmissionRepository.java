// src/main/java/com/example/lms/repository/TestSubmissionRepository.java
package com.example.lms.repository;

import com.example.lms.entity.TestSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TestSubmissionRepository extends JpaRepository<TestSubmission, Integer> {
    List<TestSubmission> findByWeeklyTestTestId(Integer testId);

    @Query("SELECT ts FROM TestSubmission ts WHERE ts.submissionDate BETWEEN :startDate AND :endDate")
    List<TestSubmission> findBySubmissionDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT ts FROM TestSubmission ts WHERE ts.user.userId = :userId AND ts.submissionDate BETWEEN :startDate AND :endDate")
    List<TestSubmission> findByUserAndSubmissionDateBetween(
            @Param("userId") Integer userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
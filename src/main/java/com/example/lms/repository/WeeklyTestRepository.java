// src/main/java/com/example/lms/repository/WeeklyTestRepository.java
package com.example.lms.repository;

import com.example.lms.entity.WeeklyTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyTestRepository extends JpaRepository<WeeklyTest, Integer> {
}
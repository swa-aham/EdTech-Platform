// src/main/java/com/example/lms/repository/CourseRepository.java
package com.example.lms.repository;

import com.example.lms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
}
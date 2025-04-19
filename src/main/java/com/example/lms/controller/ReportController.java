// src/main/java/com/example/lms/controller/ReportController.java
package com.example.lms.controller;

import com.example.lms.dto.AttendanceRecordDTO;
import com.example.lms.dto.MonthlyScoreDTO;
import com.example.lms.dto.OverallProgressDTO;
import com.example.lms.dto.StudentScoreDTO;
import com.example.lms.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    /**
     * Endpoint to get scores for a specific weekly test
     * @param testId The ID of the test
     * @return List of student names and scores for the specified test
     */
    @GetMapping("/weekly-scores")
    public ResponseEntity<List<StudentScoreDTO>> getWeeklyScores(@RequestParam Integer testId) {
        long startTime = System.nanoTime();
        List<StudentScoreDTO> scores = reportService.getWeeklyScores(testId);
        long endTime = System.nanoTime();
        long duration = ChronoUnit.MILLIS.between(LocalDateTime.now().minus(endTime - startTime, ChronoUnit.NANOS), LocalDateTime.now());
        logger.info("Request for weekly scores for test ID {} processed in {} ms", testId, duration);
        return ResponseEntity.ok(scores);
    }

    /**
     * Endpoint to get all student scores for a specific month
     * @param month The month (1-12)
     * @param year The year (e.g., 2025)
     * @return List of students and their scores for all tests in the specified month
     */
    @GetMapping("/monthly-scores")
    public ResponseEntity<List<MonthlyScoreDTO>> getMonthlyScores(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        long startTime = System.nanoTime();
        List<MonthlyScoreDTO> scores = reportService.getMonthlyScores(month, year);
        long endTime = System.nanoTime();
        long duration = ChronoUnit.MILLIS.between(LocalDateTime.now().minus(endTime - startTime, ChronoUnit.NANOS), LocalDateTime.now());
        logger.info("Request for monthly scores for month {} year {} processed in {} ms", month, year, duration);
        return ResponseEntity.ok(scores);
    }

    /**
     * Endpoint to get attendance records for a specific month
     * @param month The month (1-12)
     * @param year The year (e.g., 2025)
     * @return List of students and their attendance status for each day in the specified month
     */
    @GetMapping("/monthly-attendance")
    public ResponseEntity<List<AttendanceRecordDTO>> getMonthlyAttendance(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        long startTime = System.nanoTime();
        List<AttendanceRecordDTO> attendance = reportService.getMonthlyAttendance(month, year);
        long endTime = System.nanoTime();
        long duration = ChronoUnit.MILLIS.between(LocalDateTime.now().minus(endTime - startTime, ChronoUnit.NANOS), LocalDateTime.now());
        logger.info("Request for monthly attendance for month {} year {} processed in {} ms", month, year, duration);
        return ResponseEntity.ok(attendance);
    }

    /**
     * Endpoint to get overall progress (average score) for each student within a date range
     * @param startTimestamp The start date/time for the report
     * @param endTimestamp The end date/time for the report
     * @return List of students and their average scores within the specified time frame
     */
    @GetMapping("/overall-progress")
    public ResponseEntity<List<OverallProgressDTO>> getOverallProgress(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTimestamp,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTimestamp) {
        long startTime = System.nanoTime();
        List<OverallProgressDTO> progress = reportService.getOverallProgress(startTimestamp, endTimestamp);
        long endTime = System.nanoTime();
        long duration = ChronoUnit.MILLIS.between(LocalDateTime.now().minus(endTime - startTime, ChronoUnit.NANOS), LocalDateTime.now());
        logger.info("Request for overall progress from {} to {} processed in {} ms", startTimestamp, endTimestamp, duration);
        return ResponseEntity.ok(progress);
    }
}
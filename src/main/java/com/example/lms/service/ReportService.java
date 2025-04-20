// src/main/java/com/example/lms/service/ReportService.java
package com.example.lms.service;

import com.example.lms.dto.AttendanceRecordDTO;
import com.example.lms.dto.MonthlyScoreDTO;
import com.example.lms.dto.OverallProgressDTO;
import com.example.lms.dto.StudentScoreDTO;
import com.example.lms.entity.Attendance;
import com.example.lms.entity.TestSubmission;
import com.example.lms.entity.User;
import com.example.lms.repository.AttendanceRepository;
import com.example.lms.repository.TestSubmissionRepository;
import com.example.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private TestSubmissionRepository testSubmissionRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    /**
     * Generates a report of student scores for a specific weekly test
     * @param testId The ID of the test
     * @return List of StudentScoreDTO objects containing student names and scores
     */
    public List<StudentScoreDTO> getWeeklyScores(Integer testId) {

        String cacheKey = "weekly_scores:" + testId;

        // Check if the scores are already cached in Redis
        logger.info("Checking Redis cache for key: {}", cacheKey);
        List<StudentScoreDTO> cachedScores = redisService.get(cacheKey, List.class);

        if (cachedScores != null) {
            logger.info("Cache hit for key: {}", cacheKey);
            return cachedScores;
        }

        logger.info("Cache miss for key: {}", cacheKey);
        // If not cached, fetch from the database
        List<TestSubmission> submissions = testSubmissionRepository.findByWeeklyTestTestId(testId);

        List<StudentScoreDTO> scores = submissions.stream()
                .map(submission -> new StudentScoreDTO(
                        submission.getUser().getUserId(),
                        submission.getUser().getFullName(),
                        submission.getScore(),
                        submission.getWeeklyTest().getTestName()))
                .collect(Collectors.toList());

        // Cache the scores in Redis for 1 hour
        logger.info("Caching result for key: {} with TTL: {} seconds", cacheKey, 3600L);
        redisService.set(cacheKey, scores, 3600L);

        return scores;
    }

    /**
     * Generates a monthly report of all student scores for all tests taken in the specified month
     * @param month The month (1-12)
     * @param year The year (e.g., 2025)
     * @return List of MonthlyScoreDTO objects containing student names and their scores for each test
     */
    public List<MonthlyScoreDTO> getMonthlyScores(int month, int year) {

        String cacheKey = "monthly_scores:" + year + ":" + month;

        logger.info("Checking Redis cache for key: {}", cacheKey);
        List<MonthlyScoreDTO> cachedScores = redisService.get(cacheKey, List.class);

        if (cachedScores != null) {
            logger.info("Cache hit for key: {}", cacheKey);
            return cachedScores;
        }

        logger.info("Cache miss for key: {}", cacheKey);
        // Get start and end date for the specified month
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        // Get all submissions for the month
        List<TestSubmission> submissions = testSubmissionRepository.findBySubmissionDateBetween(startDateTime, endDateTime);

        // Group submissions by student
        Map<Integer, List<TestSubmission>> submissionsByStudent = submissions.stream()
                .collect(Collectors.groupingBy(sub -> sub.getUser().getUserId()));

        // Create DTOs for each student
        List<MonthlyScoreDTO> result = new ArrayList<>();

        for (Map.Entry<Integer, List<TestSubmission>> entry : submissionsByStudent.entrySet()) {
            Integer studentId = entry.getKey();
            List<TestSubmission> studentSubmissions = entry.getValue();

            // Skip if no submissions or not a student
            if (studentSubmissions.isEmpty() ||
                    studentSubmissions.get(0).getUser().getRole() != User.Role.student) {
                continue;
            }

            String studentName = studentSubmissions.get(0).getUser().getFullName();

            // Create a map of test names to scores
            Map<String, Object> testScores = new HashMap<>();
            for (TestSubmission submission : studentSubmissions) {
                String testName = submission.getWeeklyTest().getTestName();
                testScores.put(testName, submission.getScore());
            }

            // Cache the result in Redis for 1 day (86400 seconds)
            logger.info("Caching result for key: {} with TTL: {} seconds", cacheKey, 86400L);
            redisService.set(cacheKey, result, 86400L);
            result.add(new MonthlyScoreDTO(studentId, studentName, testScores));
        }

        return result;
    }

    /**
     * Generates a monthly attendance report for all students
     * @param month The month (1-12)
     * @param year The year (e.g., 2025)
     * @return List of AttendanceRecordDTO objects containing student names and their attendance status for each day
     */
    public List<AttendanceRecordDTO> getMonthlyAttendance(int month, int year) {

        String cacheKey = "monthly_attendance:" + year + ":" + month;
        logger.info("Checking Redis cache for key: {}", cacheKey);
        List<AttendanceRecordDTO> cachedResult = redisService.get(cacheKey, List.class);

        if (cachedResult != null) {
            logger.info("Cache hit for key: {}", cacheKey);
            return cachedResult;
        }
        logger.info("Cache miss for key: {}", cacheKey);

        // Get start and end date for the specified month
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        // Get all attendance records for the month
        List<Attendance> attendanceRecords = attendanceRepository.findByAttendanceDateBetween(startDate, endDate);

        // Group attendance records by student
        Map<Integer, List<Attendance>> attendanceByStudent = attendanceRecords.stream()
                .collect(Collectors.groupingBy(att -> att.getUser().getUserId()));

        // Create DTOs for each student
        List<AttendanceRecordDTO> result = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Find all students
        List<User> students = userRepository.findByRole(User.Role.student);

        for (User student : students) {
            Integer studentId = student.getUserId();
            String studentName = student.getFullName();

            // Get this student's attendance records
            List<Attendance> studentAttendance = attendanceByStudent.getOrDefault(studentId, Collections.emptyList());

            // Create a map of dates to attendance status
            Map<String, String> attendanceByDate = new HashMap<>();

            // Populate with all days in the month
            LocalDate date = startDate;
            while (!date.isAfter(endDate)) {
                String dateStr = date.format(dateFormatter);
                attendanceByDate.put(dateStr, "not_recorded");
                date = date.plusDays(1);
            }

            // Update with actual attendance records
            for (Attendance record : studentAttendance) {
                String dateStr = record.getAttendanceDate().format(dateFormatter);
                attendanceByDate.put(dateStr, record.getStatus().toString());
            }

            result.add(new AttendanceRecordDTO(studentId, studentName, attendanceByDate));
        }


        // Cache the result in Redis for 1 day (86400 seconds)
        logger.info("Caching result for key: {} with TTL: {} seconds", cacheKey, 86400L);
        redisService.set(cacheKey, result, 86400L);

        return result;
    }

    /**
     * Calculates the overall progress (average score) for each student within a date range
     * @param startDate The start date for the report
     * @param endDate The end date for the report
     * @return List of OverallProgressDTO objects containing student names and their average scores
     */
    public List<OverallProgressDTO> getOverallProgress(LocalDateTime startDate, LocalDateTime endDate) {
        
        
        String cacheKey = "overall_progress:" + startDate + ":" + endDate;
        logger.info("Checking Redis cache for key: {}", cacheKey);
        List<OverallProgressDTO> cachedResult = redisService.get(cacheKey, List.class);
        if (cachedResult != null) {
            logger.info("Cache hit for key: {}", cacheKey);
            return cachedResult;
        }

        logger.info("Cache miss for key: {}", cacheKey);
        // Get all submissions within the date range
        
        // Find all students
        List<User> students = userRepository.findByRole(User.Role.student);
        List<OverallProgressDTO> result = new ArrayList<>();

        for (User student : students) {
            // Get all submissions for this student in the date range
            List<TestSubmission> submissions = testSubmissionRepository.findByUserAndSubmissionDateBetween(
                    student.getUserId(), startDate, endDate);

            if (submissions.isEmpty()) {
                // No submissions for this student in the date range
                result.add(new OverallProgressDTO(
                        student.getUserId(),
                        student.getFullName(),
                        BigDecimal.ZERO,
                        0));
                continue;
            }

            // Calculate average score
            BigDecimal totalScore = submissions.stream()
                    .map(TestSubmission::getScore)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal averageScore = totalScore.divide(
                    BigDecimal.valueOf(submissions.size()), 2, RoundingMode.HALF_UP);

            result.add(new OverallProgressDTO(
                    student.getUserId(),
                    student.getFullName(),
                    averageScore,
                    submissions.size()));
        }

        // Cache the result in Redis for 1 day (86400 seconds)
        logger.info("Caching result for key: {} with TTL: {} seconds", cacheKey, 86400L);
        redisService.set(cacheKey, result, 86400L);

        return result;
    }
}

// src/main/java/com/example/lms/dto/OverallProgressDTO.java
package com.example.lms.dto;

import java.math.BigDecimal;

public class OverallProgressDTO {
    private Integer studentId;
    private String studentName;
    private BigDecimal averageScore;
    private Integer testsCompleted;

    public OverallProgressDTO() {}

    public OverallProgressDTO(Integer studentId, String studentName, BigDecimal averageScore, Integer testsCompleted) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.averageScore = averageScore;
        this.testsCompleted = testsCompleted;
    }

    // Getters and setters
    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public BigDecimal getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(BigDecimal averageScore) {
        this.averageScore = averageScore;
    }

    public Integer getTestsCompleted() {
        return testsCompleted;
    }

    public void setTestsCompleted(Integer testsCompleted) {
        this.testsCompleted = testsCompleted;
    }
}

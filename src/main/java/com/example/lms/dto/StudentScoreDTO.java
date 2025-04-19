// src/main/java/com/example/lms/dto/StudentScoreDTO.java
package com.example.lms.dto;

import java.math.BigDecimal;

public class StudentScoreDTO {
    private Integer studentId;
    private String studentName;
    private BigDecimal score;
    private String testName;

    public StudentScoreDTO() {}

    public StudentScoreDTO(Integer studentId, String studentName, BigDecimal score, String testName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.score = score;
        this.testName = testName;
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

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}

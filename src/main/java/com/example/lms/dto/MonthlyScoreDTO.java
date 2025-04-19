// src/main/java/com/example/lms/dto/MonthlyScoreDTO.java
package com.example.lms.dto;

import java.util.List;
import java.util.Map;

public class MonthlyScoreDTO {
    private Integer studentId;
    private String studentName;
    private Map<String, Object> testScores; // test name -> score

    public MonthlyScoreDTO() {}

    public MonthlyScoreDTO(Integer studentId, String studentName, Map<String, Object> testScores) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.testScores = testScores;
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

    public Map<String, Object> getTestScores() {
        return testScores;
    }

    public void setTestScores(Map<String, Object> testScores) {
        this.testScores = testScores;
    }
}

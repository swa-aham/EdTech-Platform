// src/main/java/com/example/lms/entity/TestSubmission.java
package com.example.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TestSubmissions")
public class TestSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer submissionId;

    @ManyToOne
    @JoinColumn(name = "TestID", nullable = false)
    private WeeklyTest weeklyTest;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column
    private BigDecimal score;

    @Column
    private LocalDateTime submissionDate = LocalDateTime.now();

    // Getters and setters
    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public WeeklyTest getWeeklyTest() {
        return weeklyTest;
    }

    public void setWeeklyTest(WeeklyTest weeklyTest) {
        this.weeklyTest = weeklyTest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }
}

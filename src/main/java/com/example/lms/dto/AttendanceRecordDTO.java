// src/main/java/com/example/lms/dto/AttendanceRecordDTO.java
package com.example.lms.dto;

import java.time.LocalDate;
import java.util.Map;

public class AttendanceRecordDTO {
    private Integer studentId;
    private String studentName;
    private Map<String, String> attendanceByDate; // date -> status

    public AttendanceRecordDTO() {}

    public AttendanceRecordDTO(Integer studentId, String studentName, Map<String, String> attendanceByDate) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.attendanceByDate = attendanceByDate;
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

    public Map<String, String> getAttendanceByDate() {
        return attendanceByDate;
    }

    public void setAttendanceByDate(Map<String, String> attendanceByDate) {
        this.attendanceByDate = attendanceByDate;
    }
}

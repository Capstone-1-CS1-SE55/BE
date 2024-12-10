package com.example.classmanager.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface StudentAssignmentProjection {
    Long getStudentId();

    Long getAssignmentId();

    String getStudentName();

    String getEmail();

    String getGender();

    String getPhoneNumber();

    LocalDate getBirthday();

    BigDecimal getGrade();

    String getStatus();

    LocalDateTime getTimeToWork();
}

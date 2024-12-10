package com.example.classmanager.dto;

import jakarta.persistence.Column;

import java.time.LocalDate;

public interface ClassroomStudentProjection {
    Long getClassroomId();
    String getClassroomName();
    String getStudentName();
}

package com.example.classmanager.dto.projection;

import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AssignmentOfTeacher {
    Long getAssignmentId();
    Long getTeacherId();
    String getUsername();
    String getTeacherName();
    Long getClassroomId();
    String getTitle();
    LocalDateTime getCreatedDate();
    LocalDateTime getStartDate();
    LocalDateTime getDueDate();
}

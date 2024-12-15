package com.example.classmanager.dto.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AssignmentOfTeacher {
    Long getAssignmentId();

    Long getTeacherId();

    String getUsername();
    String getTeacherName();
    String getClassroomName();
    String getTitle();
    LocalDateTime getCreatedDate();
    LocalDateTime getDueDate();
}

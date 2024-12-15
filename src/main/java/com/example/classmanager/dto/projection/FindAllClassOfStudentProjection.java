package com.example.classmanager.dto.projection;

import java.time.LocalDate;

public interface FindAllClassOfStudentProjection {
    Long getStudentId();
    Long getClassroomId();
    Integer getQuantity();
    LocalDate getCreatedDate();
    Integer getNumber();
    String getClassroomName();
    String getTeacherName();
}

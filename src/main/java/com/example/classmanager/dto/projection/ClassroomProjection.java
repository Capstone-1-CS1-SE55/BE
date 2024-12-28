package com.example.classmanager.dto.projection;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ClassroomProjection {
    Long getClassroomId();
    String getClassroomName();
    LocalDate getCreatedDate();
    Integer getQuantity();
}

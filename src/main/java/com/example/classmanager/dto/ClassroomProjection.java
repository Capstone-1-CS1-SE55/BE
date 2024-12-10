package com.example.classmanager.dto;

import java.time.LocalDate;

public interface ClassroomProjection {
    Long getClassroomId();
    String getClassroomName();
    LocalDate getCreatedDate();
    Integer getQuantity();
}

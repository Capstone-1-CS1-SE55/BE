package com.example.classmanager.dto.projection;

import java.time.LocalDate;

public interface ClassroomProjection {
    Long getClassroomId();
    String getClassroomName();
    LocalDate getCreatedDate();
    Integer getQuantity();
}

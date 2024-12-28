package com.example.classmanager.dto.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public interface AssignmentOfClassProjection {
    Long getAssignmentId();
    String getTitle();
    LocalDateTime getStartDate();
    LocalDateTime getDueDate();
    String getStatus();
    BigDecimal getGrade();
}

package com.example.classmanager.dto.projection;

import java.time.LocalDateTime;

public interface AssignmentProjection {
     String getTitle();
     LocalDateTime getStartDate();
     LocalDateTime getDueDate();
}

package com.example.classmanager.dto.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface StudentAnswerProjection {
    Long getQuestionId();
    Long getStudentId();
    Long getAssignmentId();
    String getQuestionText();
    String getAnswerText();
    BigDecimal getScore();
    BigDecimal getMax();
    String getTitle();
    LocalDateTime getStartDate();
    LocalDateTime getDueDate();
}

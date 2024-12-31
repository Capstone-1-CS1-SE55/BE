package com.example.classmanager.dto.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface QuestionProjection {
    Long getQuestionId();
    Long getAssignmentId();
    String getQuestionText();
    String getCorrectAnswer();
    BigDecimal getMaxScore();
    String getTitle();
    LocalDateTime getStartDate();
    LocalDateTime getDueDate();
    Long getClassId();
}

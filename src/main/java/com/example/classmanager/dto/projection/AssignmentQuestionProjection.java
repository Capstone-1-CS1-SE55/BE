package com.example.classmanager.dto.projection;

import java.math.BigDecimal;
public interface AssignmentQuestionProjection {
     Long getQuestionId();
     BigDecimal getMaxScore();
     String getQuestionText();
}

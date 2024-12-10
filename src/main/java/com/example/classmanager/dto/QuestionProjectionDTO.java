package com.example.classmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionProjectionDTO {
    private Long assignmentId;
    private Long questionId;
    private String questionText;
    private String correctAnswer;
    private BigDecimal maxScore;
    private String title;
    private LocalDateTime startDate;
}

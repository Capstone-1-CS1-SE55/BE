package com.example.classmanager.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnswerScoreDto {
    private Long questionId;
    private Long studentId;
    private Long assignmentId;
    private String questionText;
    private String answerText;
    private BigDecimal score;
    private BigDecimal max;
    private String title;
    private LocalDateTime startDate;
}

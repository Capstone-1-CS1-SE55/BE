package com.example.classmanager.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateDto {
    private Long questionId;
    private String questionText;
    private BigDecimal maxScore;
    private String correctAnswer;
}

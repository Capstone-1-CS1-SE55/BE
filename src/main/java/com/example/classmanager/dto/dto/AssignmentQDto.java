package com.example.classmanager.dto.dto;

import com.example.classmanager.dto.projection.AssignmentQuestionProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentQDto {
     private String title;
     private LocalDateTime startDate;
     private LocalDateTime dueDate;
     private List<AssignmentQuestionProjection> listQuestion;
}

package com.example.classmanager.dto.dto;

import com.example.classmanager.Model.Classroom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDto {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Classroom classroom;
    private List<QuestionDto> questions;
}

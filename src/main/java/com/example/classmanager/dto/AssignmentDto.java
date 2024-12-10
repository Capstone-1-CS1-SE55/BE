package com.example.classmanager.dto;

import com.example.classmanager.Model.Classroom;
import com.example.classmanager.Model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

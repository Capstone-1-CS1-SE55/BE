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
public class CreateAssignment {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private List<QuestionDto> questions;
    private List<Classroom> classrooms;
}

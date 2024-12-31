package com.example.classmanager.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentOfTeacherDTO {
    private Long assignmentId;
    private Long teacherId;
    private String username;
    private String teacherName;
    private String classroomName;
    private String title;
    private LocalDateTime createdDate;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
}

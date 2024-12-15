package com.example.classmanager.Service.StudentAssignment;

import com.example.classmanager.dto.projection.StudentAssignmentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IStudentAssignmentService {
    Page<StudentAssignmentProjection> pageGetAllStudentAssignment(Long assignmentId, String studentName ,String username ,Pageable pageable);
}

package com.example.classmanager.Service.StudentAssignment;

import com.example.classmanager.Repository.IStudentAssignmentRepository;
import com.example.classmanager.dto.projection.StudentAssignmentProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class StudentAssignmentService implements IStudentAssignmentService{
    @Autowired
    private IStudentAssignmentRepository iStudentAssignmentRepository;

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    public Page<StudentAssignmentProjection> pageGetAllStudentAssignment(Long assignmentId, String studentName, String username, Pageable pageable) {
        return iStudentAssignmentRepository.pageGetAllStudentAssignment(assignmentId, studentName, username, pageable);
    }
}

package com.example.classmanager.Service.Assignment;

import com.example.classmanager.Model.Assignment;
import com.example.classmanager.Model.Question;
import com.example.classmanager.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAssignmentService {
    List<TeacherHomeworkProjection> findAssignmentsByTeacherId(Long teacherId);
    void createNewAssignment(CreateAssignment createAssignment);
    void deleteAssignmentWithRelations(Long assignmentId);
    Assignment findById(Long assignmentId);
    void updateAssignment(List<QuestionProjectionDTO> assignmentList, Long assignmentId, String username);
    Page<AssignmentOfTeacher> pageFindAssignmentsByTeacherId(String username, String title, Pageable pageable);
}

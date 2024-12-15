package com.example.classmanager.Service.Assignment;

import com.example.classmanager.Model.Assignment;
import com.example.classmanager.dto.projection.AssignmentOfClassProjection;
import com.example.classmanager.dto.dto.CreateAssignment;
import com.example.classmanager.dto.dto.QuestionProjectionDTO;
import com.example.classmanager.dto.projection.AssignmentOfTeacher;
import com.example.classmanager.dto.projection.TeacherHomeworkProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAssignmentService {
    List<TeacherHomeworkProjection> findAssignmentsByTeacherId(Long teacherId);
    void createNewAssignment(CreateAssignment createAssignment);
    void deleteAssignmentWithRelations(Long assignmentId);
    Assignment findById(Long assignmentId);
    void updateAssignment(List<QuestionProjectionDTO> assignmentList, Long assignmentId, String username);
    Page<AssignmentOfTeacher> pageFindAssignmentsByTeacherId(String username, String title, Pageable pageable);
    Page<AssignmentOfClassProjection> pageGetAssignmentOfClass(Long classroomId, Pageable pageable);
}

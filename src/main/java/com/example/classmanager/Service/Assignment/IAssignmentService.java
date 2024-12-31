package com.example.classmanager.Service.Assignment;

import com.example.classmanager.Model.Assignment;
import com.example.classmanager.dto.dto.AssignmentOfTeacherDTO;
import com.example.classmanager.dto.dto.StudentAnswerDto;
import com.example.classmanager.dto.projection.AssignmentListProjection;
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
    void createNewAssignment(CreateAssignment createAssignment, String username);
    void deleteAssignmentWithRelations(Long assignmentId);
    Assignment findById(Long assignmentId);
    void updateAssignment(List<QuestionProjectionDTO> assignmentList, Long assignmentId, String username, List<Long> classroomIds);
    Page<AssignmentOfTeacherDTO> pageFindAssignmentsByTeacherId(String username, String title, Pageable pageable);
    Page<AssignmentOfClassProjection> pageGetAssignmentOfClass(Long classroomId,String username, Pageable pageable);
    void submitAssignment(List<StudentAnswerDto> list, String username, Long assignmentId);
    void updateStatusAssignmentAndStudentAssignment();
    Page<AssignmentListProjection> getAssignmentList(String username, Pageable pageable);
}

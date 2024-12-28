package com.example.classmanager.Service.Question;

import com.example.classmanager.Model.Question;
import com.example.classmanager.dto.dto.AssignmentQDto;
import com.example.classmanager.dto.dto.StudentAnswerDto;
import com.example.classmanager.dto.projection.AssignmentQuestionProjection;
import com.example.classmanager.dto.projection.QuestionProjection;
import com.example.classmanager.dto.dto.QuestionUpdateDto;
import com.example.classmanager.dto.projection.StudentAnswerProjection;

import java.util.List;

public interface IQuestionService {
    List<QuestionProjection> getAllQuestionsInAssignment(Long assignmentId, String username);
    void updateQuestionAndTeacherAnswer(QuestionUpdateDto questionUpdateDto);
    void deleteQuestionWithRelations(Long questionId);
    List<StudentAnswerProjection> getAllStudentAnswer(Long assignmentId, Long studentId, String username);
    AssignmentQDto getAllQuestionInAssignment(Long assignmentId, String username);
    Question findQuestionById(Long questionId);
    void saveStudentAnswer(List<StudentAnswerDto> list, String username);
}

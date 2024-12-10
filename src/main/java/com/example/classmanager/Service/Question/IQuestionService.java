package com.example.classmanager.Service.Question;

import com.example.classmanager.dto.QuestionProjection;
import com.example.classmanager.dto.QuestionUpdateDto;
import com.example.classmanager.dto.StudentAnswerProjection;

import java.util.List;

public interface IQuestionService {
    List<QuestionProjection> getAllQuestionsInAssignment(Long assignmentId, String username);
    void updateQuestionAndTeacherAnswer(QuestionUpdateDto questionUpdateDto);
    void deleteQuestionWithRelations(Long questionId);
    List<StudentAnswerProjection> getAllStudentAnswer(Long assignmentId, Long studentId, String username);
}

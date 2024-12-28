package com.example.classmanager.Service.StudentAnswer;

import com.example.classmanager.Model.StudentAnswer;
import com.example.classmanager.dto.dto.StudentAnswerScoreDto;
import com.example.classmanager.dto.projection.StudentAnswerProjection;

import java.util.List;

public interface IStudentAnswerService {
    void saveStudentAnswers(StudentAnswer studentAnswer);
    void testScoring(List<StudentAnswerScoreDto> list, Long assignmentId, Long studentId);
}

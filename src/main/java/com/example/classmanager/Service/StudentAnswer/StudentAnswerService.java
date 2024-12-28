package com.example.classmanager.Service.StudentAnswer;

import com.example.classmanager.Entity.StudentAnswerId;
import com.example.classmanager.Entity.StudentAssignmentId;
import com.example.classmanager.Model.*;
import com.example.classmanager.Repository.*;
import com.example.classmanager.dto.dto.StudentAnswerScoreDto;
import com.example.classmanager.dto.projection.StudentAnswerProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StudentAnswerService implements IStudentAnswerService{
    @Autowired
    private IStudentAnswerRepository iStudentAnswerRepository;

    @Autowired
    private IAssignmentRepository iAssignmentRepository;

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IStudentAssignmentRepository iStudentAssignmentRepository;
    @Override
    public void saveStudentAnswers(StudentAnswer studentAnswer) {
        iStudentAnswerRepository.save(studentAnswer);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void testScoring(List<StudentAnswerScoreDto> list, Long assignmentId, Long studentId) {
        BigDecimal count = BigDecimal.ZERO;
//        Assignment assignment = iAssignmentRepository.findById(assignmentId).orElse(null);
//        Student student = iStudentRepository.findById(studentId).orElse(null);
        for (StudentAnswerScoreDto scoreDto: list) {
            StudentAnswer studentAnswer = new StudentAnswer();
            StudentAnswerId studentAnswerId = new StudentAnswerId(studentId, scoreDto.getQuestionId());
            studentAnswer.setId(studentAnswerId);
            studentAnswer.setScore(scoreDto.getScore());
            studentAnswer.setAnswerText(scoreDto.getAnswerText());
            iStudentAnswerRepository.save(studentAnswer);
            count = count.add(scoreDto.getScore());
        }
//        StudentAssignmentId studentAssignmentId = new StudentAssignmentId(studentId, assignmentId);
        StudentAssignment studentAssignment = iStudentAssignmentRepository.findByStudentIdAndAssignmentId(studentId, assignmentId).orElse(null);
//        studentAssignment.setId(studentAssignmentId);
        studentAssignment.setGrade(count);
        iStudentAssignmentRepository.save(studentAssignment);
    }
}

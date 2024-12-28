package com.example.classmanager.Service.Question;

import com.example.classmanager.Entity.StudentAnswerId;
import com.example.classmanager.Model.Question;
import com.example.classmanager.Model.Student;
import com.example.classmanager.Model.StudentAnswer;
import com.example.classmanager.Model.TeacherAnswer;
import com.example.classmanager.Repository.*;
import com.example.classmanager.dto.dto.AssignmentQDto;
import com.example.classmanager.dto.dto.StudentAnswerDto;
import com.example.classmanager.dto.projection.AssignmentProjection;
import com.example.classmanager.dto.projection.AssignmentQuestionProjection;
import com.example.classmanager.dto.projection.QuestionProjection;
import com.example.classmanager.dto.dto.QuestionUpdateDto;
import com.example.classmanager.dto.projection.StudentAnswerProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService implements IQuestionService{
    @Autowired
    private IQuestionRepository iQuestionRepository;

    @Autowired
    private IAssignmentRepository iAssignmentRepository;

    @Autowired
    private ITeacherAnswerRepository iTeacherAnswerRepository;

    @Autowired
    private IStudentAnswerRepository iStudentAnswerRepository;

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public List<QuestionProjection> getAllQuestionsInAssignment(Long assignmentId, String username) {
        return iQuestionRepository.getAllQuestionsInAssignment(assignmentId, username);
    }

    @Override
    @Transactional
    public void updateQuestionAndTeacherAnswer(QuestionUpdateDto questionUpdateDto) {

        Question question = iQuestionRepository.findById(questionUpdateDto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));
        question.setQuestionText(questionUpdateDto.getQuestionText());
        question.setMaxScore(questionUpdateDto.getMaxScore());
        Question updatedQuestion = iQuestionRepository.save(question);

        TeacherAnswer teacherAnswer = iTeacherAnswerRepository.findById(questionUpdateDto.getQuestionId())
                .orElse(new TeacherAnswer());
        teacherAnswer.setQuestionId(updatedQuestion.getQuestionId());
        teacherAnswer.setCorrectAnswer(questionUpdateDto.getCorrectAnswer());
        iTeacherAnswerRepository.save(teacherAnswer);
    }

    @Override
    public void deleteQuestionWithRelations(Long questionId) {
        try {
            String callProcedure = "{CALL delete_question_with_relations(?)}";
            jdbcTemplate.update(callProcedure, questionId);
        } catch (Exception e) {
            System.out.println("Error occurred during stored procedure execution: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public List<StudentAnswerProjection> getAllStudentAnswer(Long assignmentId, Long studentId, String username) {
        return iQuestionRepository.getAllStudentAnswer(assignmentId, studentId, username);
    }

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public AssignmentQDto getAllQuestionInAssignment(Long assignmentId, String username) {
        List<AssignmentQuestionProjection> list = iAssignmentRepository.getAllQuestionInAssignment(assignmentId, username);
        AssignmentProjection assignmentProjection = iAssignmentRepository.getAssignmentByAssignmentId(assignmentId);
        AssignmentQDto assignmentQDto = new AssignmentQDto();
        assignmentQDto.setTitle(assignmentProjection.getTitle());
        assignmentQDto.setDueDate(assignmentProjection.getDueDate());
        assignmentQDto.setStartDate(assignmentProjection.getStartDate());
        assignmentQDto.setListQuestion(list);
        return assignmentQDto;
    }

    @Override
    public Question findQuestionById(Long questionId) {
        return iQuestionRepository.findById(questionId).orElse(null);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void saveStudentAnswer(List<StudentAnswerDto> list, String username) {
        Student student = iStudentRepository.findStudentByUsername(username).orElse(null);
        for (StudentAnswerDto studentAnswerDto : list) {
            Question question = iQuestionRepository.findById(studentAnswerDto.getId()).orElse(null);
            StudentAnswer studentAnswer = new StudentAnswer();
            StudentAnswerId studentAnswerId = new StudentAnswerId(student.getStudentId(), question.getQuestionId());
            studentAnswer.setAnswerText(studentAnswerDto.getStudentAnswer());
            studentAnswer.setId(studentAnswerId);
            iStudentAnswerRepository.save(studentAnswer);
        }
    }
}

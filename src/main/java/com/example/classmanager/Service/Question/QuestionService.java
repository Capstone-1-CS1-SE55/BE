package com.example.classmanager.Service.Question;

import com.example.classmanager.Model.Question;
import com.example.classmanager.Model.TeacherAnswer;
import com.example.classmanager.Repository.IAssignmentRepository;
import com.example.classmanager.Repository.IQuestionRepository;
import com.example.classmanager.Repository.ITeacherAnswerRepository;
import com.example.classmanager.dto.dto.AssignmentQDto;
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
    public AssignmentQDto getAllQuestionInAssignment(Long assignmentId) {
        List<AssignmentQuestionProjection> list = iAssignmentRepository.getAllQuestionInAssignment(assignmentId);
        AssignmentProjection assignmentProjection = iAssignmentRepository.getAssignmentByAssignmentId(assignmentId);
        AssignmentQDto assignmentQDto = new AssignmentQDto();
        assignmentQDto.setTitle(assignmentProjection.getTitle());
        assignmentQDto.setDueDate(assignmentProjection.getDueDate());
        assignmentQDto.setStartDate(assignmentProjection.getStartDate());
        assignmentQDto.setListQuestion(list);
        return assignmentQDto;
    }
}

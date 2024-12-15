package com.example.classmanager.Service.Assignment;

import com.example.classmanager.Model.Assignment;
import com.example.classmanager.Model.Classroom;
import com.example.classmanager.Model.Question;
import com.example.classmanager.Model.TeacherAnswer;
import com.example.classmanager.Repository.IAssignmentRepository;
import com.example.classmanager.Repository.IQuestionRepository;
import com.example.classmanager.Repository.ITeacherAnswerRepository;
import com.example.classmanager.dto.projection.AssignmentOfClassProjection;
import com.example.classmanager.dto.dto.CreateAssignment;
import com.example.classmanager.dto.dto.QuestionDto;
import com.example.classmanager.dto.dto.QuestionProjectionDTO;
import com.example.classmanager.dto.projection.AssignmentOfTeacher;
import com.example.classmanager.dto.projection.QuestionProjection;
import com.example.classmanager.dto.projection.TeacherHomeworkProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentService implements IAssignmentService {
    @Autowired
    private IAssignmentRepository iAssignmentRepository;

    @Autowired
    private IQuestionRepository iQuestionRepository;

    @Autowired
    private ITeacherAnswerRepository iTeacherAnswerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public List<TeacherHomeworkProjection> findAssignmentsByTeacherId(Long teacherId) {
        return iAssignmentRepository.findAssignmentsByTeacherId(teacherId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void createNewAssignment(CreateAssignment createAssignment) {
        for (Classroom classroom : createAssignment.getClassrooms()) {
            Assignment assignment = new Assignment();
            assignment.setTitle(createAssignment.getTitle());
            assignment.setDueDate(createAssignment.getDueDate());
            assignment.setStartDate(createAssignment.getStartDate());
            assignment.setClassroom(classroom);
            Assignment savedAssignment = iAssignmentRepository.save(assignment);
            for (QuestionDto questionDto : createAssignment.getQuestions()) {
                Question question = new Question();
                question.setQuestionText(questionDto.getQuestionText());
                question.setMaxScore(questionDto.getMaxScore());
                question.setAssignment(savedAssignment);
                Question savedQuestion = iQuestionRepository.save(question);
                TeacherAnswer teacherAnswer = new TeacherAnswer();
                teacherAnswer.setCorrectAnswer(questionDto.getCorrectAnswer());
                teacherAnswer.setQuestionId(savedQuestion.getQuestionId());
                iTeacherAnswerRepository.save(teacherAnswer);
            }
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public void deleteAssignmentWithRelations(Long assignmentId) {
        try {
            String callProcedure = "{CALL delete_assignment_with_relations(?)}";
            jdbcTemplate.update(callProcedure, assignmentId);
        } catch (Exception e) {
            System.out.println("Error occurred during stored procedure execution: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Assignment findById(Long assignmentId) {
        return iAssignmentRepository.findById(assignmentId).orElse(null);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public void updateAssignment(List<QuestionProjectionDTO> assignmentList, Long assignmentId, String username) {
        List<QuestionProjection> list = iQuestionRepository.getAllQuestionsInAssignment(assignmentId, username);
        Assignment assignment = iAssignmentRepository.findById(assignmentId).orElse(null);

        Set<Long> newQuestionIds = assignmentList.stream()
                .filter(q -> q.getQuestionId() != null)
                .map(QuestionProjectionDTO::getQuestionId)
                .collect(Collectors.toSet());

        try {

            for (QuestionProjection question : list) {
                if (!newQuestionIds.contains(question.getQuestionId())) {
                    String callProcedure = "{CALL delete_question_with_relations(?)}";
                    jdbcTemplate.update(callProcedure, question.getQuestionId());
                }
            }

            for (QuestionProjectionDTO question : assignmentList) {
                if (question.getQuestionId() != null) {
                    Optional<Question> existingQuestion = iQuestionRepository.findById(question.getQuestionId());
                    if (existingQuestion.isPresent()) {
                        Question existing = existingQuestion.get();
                        existing.setQuestionText(question.getQuestionText());
                        existing.setMaxScore(question.getMaxScore());
                        iQuestionRepository.save(existing);
                    }
                        Optional<TeacherAnswer> existingAnswer = iTeacherAnswerRepository.findByQuestionId(question.getQuestionId());
                        if (existingAnswer.isPresent()) {
                            TeacherAnswer teacherAnswer = existingAnswer.get();
                            teacherAnswer.setCorrectAnswer(question.getCorrectAnswer());
                            iTeacherAnswerRepository.save(teacherAnswer);
                        }
                } else {
                        Question newQuestion = new Question();
                        newQuestion.setQuestionText(question.getQuestionText());
                        newQuestion.setMaxScore(question.getMaxScore());
                        newQuestion.setAssignment(assignment);
                        Question savedQuestion = iQuestionRepository.save(newQuestion);

                        TeacherAnswer teacherAnswer = new TeacherAnswer();
                        teacherAnswer.setQuestionId(savedQuestion.getQuestionId());
                        teacherAnswer.setCorrectAnswer(question.getCorrectAnswer());
                        iTeacherAnswerRepository.save(teacherAnswer);
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi trong quá trình cập nhật dữ liệu: " + e.getMessage());
            throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhật dữ liệu.");
        }
    }

    @Override
    public Page<AssignmentOfTeacher> pageFindAssignmentsByTeacherId(String username, String title, Pageable pageable) {
        return iAssignmentRepository.pageFindAssignmentsByTeacherId(username, title, pageable);
    }

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public Page<AssignmentOfClassProjection> pageGetAssignmentOfClass(Long classroomId, Pageable pageable) {
        return iAssignmentRepository.pageGetAssignmentOfClass(classroomId, pageable);
    }
}

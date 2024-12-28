package com.example.classmanager.Service.Assignment;

import com.example.classmanager.Entity.StudentAnswerId;
import com.example.classmanager.Model.*;
import com.example.classmanager.Repository.*;
import com.example.classmanager.dto.dto.StudentAnswerDto;
import com.example.classmanager.dto.projection.*;
import com.example.classmanager.dto.dto.CreateAssignment;
import com.example.classmanager.dto.dto.QuestionDto;
import com.example.classmanager.dto.dto.QuestionProjectionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentService implements IAssignmentService {
    @Autowired
    private IAssignmentRepository iAssignmentRepository;

    @Autowired
    private IClassroomRepository iClassroomRepository;

    @Autowired
    private IQuestionRepository iQuestionRepository;

    @Autowired
    private ITeacherAnswerRepository iTeacherAnswerRepository;

    @Autowired
    private IClassroomStudentRepository iClassroomStudentRepository;

    @Autowired
    private IStudentAnswerRepository iStudentAnswerRepository;

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IStudentAssignmentRepository iStudentAssignmentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public List<TeacherHomeworkProjection> findAssignmentsByTeacherId(Long teacherId) {
        return iAssignmentRepository.findAssignmentsByTeacherId(teacherId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public void createNewAssignment(CreateAssignment createAssignment) {
        for (Classroom classroom : createAssignment.getClassrooms()) {
            Assignment assignment = new Assignment();
            assignment.setTitle(createAssignment.getTitle());
            assignment.setDueDate(createAssignment.getDueDate());
            assignment.setStartDate(createAssignment.getStartDate());
            assignment.setClassroom(classroom);
            List<StudentProjection> listStudentInClass = iClassroomStudentRepository.listStudentInClass(classroom.getClassroomId());
            Assignment savedAssignment = iAssignmentRepository.save(assignment);
            for (QuestionDto questionDto : createAssignment.getQuestions()) {
                Question question = new Question();
                question.setQuestionText(questionDto.getQuestionText());
                question.setMaxScore(questionDto.getMaxScore());
                question.setAssignment(savedAssignment);
                Question savedQuestion = iQuestionRepository.save(question);
                for (StudentProjection student : listStudentInClass) {
                    StudentAnswer studentAnswer = new StudentAnswer();
                    StudentAnswerId studentAnswerId = new StudentAnswerId(iStudentRepository.findById(student.getStudentId()).orElse(null).getStudentId(), savedQuestion.getQuestionId());
                    studentAnswer.setAnswerText("");
                    studentAnswer.setStudent(iStudentRepository.findById(student.getStudentId()).orElse(null));
                    studentAnswer.setQuestion(savedQuestion);
                    studentAnswer.setScore(null);
                    studentAnswer.setId(studentAnswerId);
                    iStudentAnswerRepository.save(studentAnswer);
                }
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

                    Assignment assignment1 = iAssignmentRepository.findById(question.getAssignmentId()).orElse(null);
                    Classroom classroom = iClassroomRepository.findById(assignment1.getClassroom().getClassroomId()).orElse(null);
                    List<StudentProjection> listStudentInClass = iClassroomStudentRepository.listStudentInClass(classroom.getClassroomId());
                    for (StudentProjection student : listStudentInClass) {
                        StudentAnswer studentAnswer = new StudentAnswer();
                        StudentAnswerId studentAnswerId = new StudentAnswerId(iStudentRepository.findById(student.getStudentId()).orElse(null).getStudentId(), savedQuestion.getQuestionId());
                        studentAnswer.setAnswerText("");
                        studentAnswer.setStudent(iStudentRepository.findById(student.getStudentId()).orElse(null));
                        studentAnswer.setQuestion(savedQuestion);
                        studentAnswer.setScore(null);
                        studentAnswer.setId(studentAnswerId);
                        iStudentAnswerRepository.save(studentAnswer);
                    }

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
    public Page<AssignmentOfClassProjection> pageGetAssignmentOfClass(Long classroomId, String username, Pageable pageable) {
        return iAssignmentRepository.pageGetAssignmentOfClass(classroomId, username, pageable);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void submitAssignment(List<StudentAnswerDto> list, String username, Long assignmentId) {
        Student student = iStudentRepository.findStudentByUsername(username).orElse(null);
        for (StudentAnswerDto studentAnswerDto : list) {
            Question question = iQuestionRepository.findById(studentAnswerDto.getId()).orElse(null);
            StudentAnswer studentAnswer = new StudentAnswer();
            StudentAnswerId studentAnswerId = new StudentAnswerId(student.getStudentId(), question.getQuestionId());
            studentAnswer.setAnswerText(studentAnswerDto.getStudentAnswer());
            studentAnswer.setId(studentAnswerId);
            studentAnswer.setScore(BigDecimal.ZERO);
            iStudentAnswerRepository.save(studentAnswer);
        }
        StudentAssignment studentAssignment = iStudentAssignmentRepository.findByStudentIdAndAssignmentId(student.getStudentId(), assignmentId).orElse(null);
        studentAssignment.setStatus("Đã nộp");
        iStudentAssignmentRepository.save(studentAssignment);
    }

    @Override
    @Transactional
    public void updateStatusAssignmentAndStudentAssignment() {
        iAssignmentRepository.updateStatusAssignment();
        iStudentAssignmentRepository.updateStatusStudentAssignment();
    }

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public Page<AssignmentListProjection> getAssignmentList(String username, Pageable pageable) {
        return iAssignmentRepository.getAssignmentList(username, pageable);
    }
}

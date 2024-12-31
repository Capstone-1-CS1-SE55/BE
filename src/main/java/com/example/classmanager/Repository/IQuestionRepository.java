package com.example.classmanager.Repository;

import com.example.classmanager.Model.Question;
import com.example.classmanager.dto.projection.QuestionProjection;
import com.example.classmanager.dto.projection.StudentAnswerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IQuestionRepository extends JpaRepository<Question, Long> {
    @Query("select q.questionText as questionText, ta.correctAnswer as correctAnswer, q.maxScore as maxScore, q.questionId as questionId, " +
            "q.assignment.title as title, q.assignment.startDate as startDate, q.assignment.dueDate as dueDate, q.assignment.assignmentId as assignmentId, " +
            "q.assignment.classroom.classroomId as classId " +
            "from Question q join TeacherAnswer ta " +
            "on q.questionId = ta.question.questionId " +
            "where q.assignment.assignmentId = :assignmentId and q.assignment.teacher.user.username = :username")
    List<QuestionProjection> getAllQuestionsInAssignment(@Param("assignmentId") Long assignmentId, @Param("username") String username);

    @Query("select q.questionText as questionText, sa.answerText as answerText, sa.score as score, q.questionId as questionId, s.studentId as studentId, " +
            "q.assignment.title as title, q.assignment.startDate as startDate, q.assignment.dueDate as dueDate, q.assignment.assignmentId as assignmentId, q.maxScore as max from Question q " +
            "join StudentAnswer sa on q.questionId = sa.question.questionId " +
            "join Student s on s.studentId = sa.student.studentId where" +
            " sa.question.assignment.assignmentId = :assignmentId and sa.student.studentId = :studentId " +
            "and q.assignment.teacher.user.username = :username")
    List<StudentAnswerProjection> getAllStudentAnswer(@Param("assignmentId") Long assignmentId, @Param("studentId") Long studentId, @Param("username") String username);

}

package com.example.classmanager.Repository;

import com.example.classmanager.Model.Assignment;
import com.example.classmanager.dto.projection.*;
import com.example.classmanager.dto.dto.AssignmentQDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAssignmentRepository extends JpaRepository<Assignment, Long> {
    @Query("select t.teacherId as teacherId, t.teacherName as teacherName, c.classroomName as classroomName, " +
            "a.title as title from Teacher t join " +
            "Classroom c on t.teacherId = c.teacher.teacherId join Assignment a " +
            "on a.classroom.classroomId = c.classroomId where t.teacherId = :teacherId")
    List<TeacherHomeworkProjection> findAssignmentsByTeacherId(@Param("teacherId") Long teacherId);

    @Query("select t.teacherId as teacherId, t.teacherName as teacherName, c.classroomName as classroomName, " +
            "a.title as title, u.username as username, a.createdDate as createdDate, a.dueDate as dueDate, " +
            "a.assignmentId as assignmentId from Teacher t join " +
            "Classroom c on t.teacherId = c.teacher.teacherId join Assignment a " +
            "on a.classroom.classroomId = c.classroomId join User u on t.user.userId = u.userId where u.username = :username and a.title like :title")
    Page<AssignmentOfTeacher> pageFindAssignmentsByTeacherId(@Param("username") String username, @Param("title") String title, Pageable pageable);

    @Query("select a.assignmentId as assignmentId, a.title as title, a.startDate as startDate, " +
            "a.dueDate as dueDate, a.status as status from Assignment a " +
            "where a.classroom.classroomId = :classroomId")
    Page<AssignmentOfClassProjection> pageGetAssignmentOfClass(@Param("classroomId") Long classroomId, Pageable pageable);

    @Query("select q.questionId as questionId, q.questionText as questionText, q.maxScore as maxScore " +
            "from Question q join q.assignment a where a.assignmentId = :assignmentId")
    List<AssignmentQuestionProjection> getAllQuestionInAssignment(@Param("assignmentId") Long assignmentId);

    @Query("select a.title as title, a.startDate as startDate, a.dueDate as dueDate " +
            "from Assignment a where a.assignmentId = :assignmentId")
    AssignmentProjection getAssignmentByAssignmentId(@Param("assignmentId") Long assignmentId);
}

package com.example.classmanager.Repository;

import com.example.classmanager.Model.Assignment;
import com.example.classmanager.dto.AssignmentOfTeacher;
import com.example.classmanager.dto.TeacherHomeworkProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
}

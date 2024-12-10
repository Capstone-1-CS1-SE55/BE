package com.example.classmanager.Repository;

import com.example.classmanager.Model.StudentAssignment;
import com.example.classmanager.dto.StudentAssignmentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IStudentAssignmentRepository extends JpaRepository<StudentAssignment, Long> {
    @Query("select sa.student.studentId as studentId, sa.assignment.assignmentId as assignmentId, " +
            "sa.student.studentName as studentName, sa.student.email as email, sa.student.gender as gender, " +
            "sa.student.birthday as birthday, sa.student.phoneNumber as phoneNumber, sa.status as status, " +
            "sa.grade as grade, sa.timeToWork as timeToWork from StudentAssignment sa where " +
            "sa.assignment.assignmentId = :assignmentId and sa.student.studentName like :studentName and " +
            "sa.assignment.classroom.teacher.user.username = :username")
    Page<StudentAssignmentProjection> pageGetAllStudentAssignment(@Param("assignmentId") Long assignmentId,@Param("studentName") String studentName ,@Param("username") String username,Pageable pageable);
}

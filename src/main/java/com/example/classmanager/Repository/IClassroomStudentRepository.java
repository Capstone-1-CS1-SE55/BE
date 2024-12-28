package com.example.classmanager.Repository;

import com.example.classmanager.Model.ClassroomStudent;
import com.example.classmanager.Model.Student;
import com.example.classmanager.dto.projection.StudentProjection;
import com.example.classmanager.dto.projection.FindAllClassOfStudentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IClassroomStudentRepository extends JpaRepository<ClassroomStudent, Long> {
    @Query("SELECT CASE WHEN COUNT(cs) > 0 THEN true ELSE false END " +
            "FROM ClassroomStudent cs WHERE cs.student.studentId = :studentId AND cs.classroom.classroomId = :classroomId")
    Boolean existsStudentInClassroom(@Param("studentId") Long studentId, @Param("classroomId") Long classroomId);

    @Query("select c.classroomId as classroomId, c.classroomName as classroomName, c.createdDate as createdDate, c.teacher.teacherName as teacherName, " +
            "c.quantity as quantity, cs.student.studentId as studentId, count(a.classroom.classroomId) as number from ClassroomStudent cs " +
            "join Classroom c on cs.classroom.classroomId = c.classroomId join Assignment a on " +
            "c.classroomId = a.classroom.classroomId where cs.student.user.username = :username and c.classroomName like %:classroomName% " +
            "group by c.classroomId, cs.student.studentId")
    Page<FindAllClassOfStudentProjection> PageGetAllClassOfStudent(@Param("username") String username, @Param("classroomName") String classroomName, Pageable pageable);
    @Query("select s.studentId as studentId, s.studentName as studentName, s.birthday as birthday, " +
            "s.email as email, s.gender as gender, cs.status as status from ClassroomStudent cs " +
            "join Student s on cs.student.studentId = s.studentId where cs.classroom.classroomId = :classroomId")
    Page<StudentProjection> findStudentByClassroomId(@Param("classroomId") Long classroomId, Pageable pageable);

    @Query("select s.studentId as studentId, s.studentName as studentName, s.birthday as birthday, " +
            "s.email as email, s.gender as gender, cs.status as status from ClassroomStudent cs " +
            "join Student s on cs.student.studentId = s.studentId where cs.classroom.classroomId = :classroomId")
    List<StudentProjection> listStudentInClass(@Param("classroomId") Long classroomId);
}

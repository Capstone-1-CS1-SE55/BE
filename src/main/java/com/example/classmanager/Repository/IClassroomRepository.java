package com.example.classmanager.Repository;

import com.example.classmanager.Model.Classroom;
import com.example.classmanager.dto.projection.ClassroomNameProjection;
import com.example.classmanager.dto.projection.ClassroomProjection;
import com.example.classmanager.dto.projection.ClassroomStudentProjection;
import com.example.classmanager.dto.projection.FindAllStudentInClassroomProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IClassroomRepository extends JpaRepository<Classroom, Long> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Classroom c WHERE c.teacher.teacherId = :id AND c.classroomName = :name")
    Boolean existsClassroomNameByTeacher(@Param("id") Long id, @Param("name") String name);

    @Modifying
    @Transactional
    @Query("UPDATE Classroom c SET c.classroomName = :name WHERE c.classroomId = :id")
    void updateClassroomNameById(@Param("id") Long id, @Param("name") String name);

    @Query("select c.classroomId as classroomId, c.classroomName as classroomName, s.studentName as studentName from Classroom c join ClassroomStudent cs " +
            "on c.classroomId = cs.classroom.classroomId " +
            "join Student s on cs.student.studentId = s.studentId where c.classroomId = :id")
    List<ClassroomStudentProjection> getStudentInClassroom(@Param("id") Long id);

    @Query("select c.classroomId as classroomId, c.classroomName as classroomName, s.studentName as studentName from Classroom c join ClassroomStudent cs " +
            "on c.classroomId = cs.classroom.classroomId " +
            "join Student s on cs.student.studentId = s.studentId where c.classroomId = :id and s.studentName like :studentName")
    List<ClassroomStudentProjection> getStudentInClassroomByStudentName(@Param("id") Long id, @Param("studentName") String studentName);

    @Query("select c.classroomId as classroomId ,c.classroomName as classroomName, c.createdDate as createdDate, c.quantity as quantity " +
            "from Classroom c where c.teacher.user.username = :username")
    List<ClassroomProjection> getClassroomsByTeacherUsername(@Param("username") String username);

    @Query("select c.classroomId as classroomId, c.classroomName as classroomName, c.createdDate as createdDate, c.quantity as quantity " +
            "from Classroom c where c.teacher.user.username = :username " +
            "and c.classroomName like :classroomName")
    Page<ClassroomProjection> PagegetClassroomsByTeacherId(@Param("username") String username, @Param("classroomName") String classroomName, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from ClassroomStudent where student.studentId = :studentId and classroom.classroomId = :classroomId")
    void deleteStudentFromClassroom(@Param("studentId") Long studentId, @Param("classroomId") Long classroomId);

    @Query("select c.classroomId as classroomId, c.classroomName as classroomName, s.studentName as studentName, " +
            "s.email as email, s.birthday as birthday, s.gender as gender, s.phoneNumber as phoneNumber, " +
            "s.studentId as studentId from Classroom c join ClassroomStudent cs " +
            "on c.classroomId = cs.classroom.classroomId " +
            "join Student s on cs.student.studentId = s.studentId where c.classroomId = :id and s.studentName like :studentName")
    Page<FindAllStudentInClassroomProjection> PageGetStudentInClassroomByStudentName(@Param("id") Long id, @Param("studentName") String studentName, Pageable pageable);

    @Query("select c.classroomName as classroomName from Classroom c where c.classroomId = :classroomId")
    Optional<ClassroomNameProjection> findClassroomName(@Param("classroomId") Long classroomId);
}

package com.example.classmanager.Repository;

import com.example.classmanager.Model.ClassroomStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IClassroomStudentRepository extends JpaRepository<ClassroomStudent, Long> {
    @Query("SELECT CASE WHEN COUNT(cs) > 0 THEN true ELSE false END " +
            "FROM ClassroomStudent cs WHERE cs.student.studentId = :studentId AND cs.classroom.classroomId = :classroomId")
    Boolean existsStudentInClassroom(@Param("studentId") Long studentId, @Param("classroomId") Long classroomId);
}

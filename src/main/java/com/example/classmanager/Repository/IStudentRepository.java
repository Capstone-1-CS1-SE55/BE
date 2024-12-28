package com.example.classmanager.Repository;

import com.example.classmanager.Model.Student;
import com.example.classmanager.Model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IStudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByEmail(String email);
    @Query("select s from Student s where s.user.username = :username")
    Optional<Student> findStudentByUsername(@Param("username") String username);
}

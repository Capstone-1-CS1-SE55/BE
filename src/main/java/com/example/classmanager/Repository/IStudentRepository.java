package com.example.classmanager.Repository;

import com.example.classmanager.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByEmail(String email);

}

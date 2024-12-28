package com.example.classmanager.Repository;

import com.example.classmanager.Entity.StudentAnswerId;
import com.example.classmanager.Model.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentAnswerRepository extends JpaRepository<StudentAnswer, StudentAnswerId> {

}

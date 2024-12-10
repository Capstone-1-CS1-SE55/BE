package com.example.classmanager.Repository;

import com.example.classmanager.Model.TeacherAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITeacherAnswerRepository extends JpaRepository<TeacherAnswer, Long> {
    Optional<TeacherAnswer> findByQuestionId(Long questionId);
}

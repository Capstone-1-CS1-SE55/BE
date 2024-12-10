package com.example.classmanager.Repository;

import com.example.classmanager.Model.Teacher;
import com.example.classmanager.Model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ITeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findTeacherByUser(User user);

    @Query("select t.teacherId as teacherId from Teacher t where t.user.username = :username")
    Long findTeacherIdByUsername(@Param("username") String username);
}

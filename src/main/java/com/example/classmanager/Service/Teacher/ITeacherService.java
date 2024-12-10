package com.example.classmanager.Service.Teacher;

import com.example.classmanager.Model.Teacher;
import com.example.classmanager.Model.login.User;
import org.springframework.data.repository.query.Param;

public interface ITeacherService {
    Teacher getTeacherById(Long teacherId);
    Teacher getInfoTeacher(User user);
    Long findTeacherIdByUsername(String username);
}

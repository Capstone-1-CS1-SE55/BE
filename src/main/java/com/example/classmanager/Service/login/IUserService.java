package com.example.classmanager.Service.login;

import com.example.classmanager.Entity.response.UserResponse;
import com.example.classmanager.Model.login.User;
import com.example.classmanager.dto.UserCreateDto;

import java.util.List;

public interface IUserService {
    void createUserTeacher(UserCreateDto userStudentDto);
    void createUserStudent(UserCreateDto userStudentDto);
    boolean existUserByName(String username);
    List<User> getAllUsers();
    User getUserById(Long id);
    UserResponse getMyInfo();

    UserResponse getUser(Long id);
    User getUserByName(String userName);
}

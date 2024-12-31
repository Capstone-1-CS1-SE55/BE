package com.example.classmanager.Service.login;

import com.example.classmanager.Entity.response.UserResponse;
import com.example.classmanager.Model.login.User;
import com.example.classmanager.dto.dto.AccountDto;
import com.example.classmanager.dto.dto.UserCreateDto;
import com.example.classmanager.dto.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserService {
    void createUserTeacher(AccountDto accountDto);
    void createUserStudent(AccountDto accountDto);
    boolean existUserByName(String username);
    List<User> getAllUsers();
    User getUserById(Long id);
    UserResponse getMyInfo();
    UserResponse getUser(Long id);
    User getUserByName(String userName);
    Page<UserProjection> pageGetAllUser(String username, String excludedRole, Pageable pageable);
    void deleteUser(Long userId);
    UserProjection checkUsername(String username);
}

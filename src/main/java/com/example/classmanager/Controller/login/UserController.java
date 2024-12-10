package com.example.classmanager.Controller.login;

import com.example.classmanager.Entity.response.ApiResponse;
import com.example.classmanager.Entity.response.UserResponse;
import com.example.classmanager.Model.Teacher;
import com.example.classmanager.Model.login.User;
import com.example.classmanager.Service.Teacher.ITeacherService;
import com.example.classmanager.Service.login.IUserService;
import com.example.classmanager.dto.UserCreateDto;
import com.example.classmanager.exception.AppException;
import com.example.classmanager.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ITeacherService iTeacherService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createNewUser(@RequestBody UserCreateDto request) {
        if (!userService.existUserByName(request.getUsername())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (request.getRole().equals("TEACHER")) {
            userService.createUserTeacher(request);
        } else if(request.getRole().equals("STUDENT")) {
            userService.createUserStudent(request);
        }
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .message("created successfully")
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {

        var authentication = SecurityContextHolder.getContext().getAuthentication(); // Chứa thông tin User đang đăng nhập hiện tại

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        List<User> list = userService.getAllUsers();
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") Long userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping("/get-all-user")
    public ResponseEntity<Teacher> getAllUsers2() {

        var authentication = SecurityContextHolder.getContext().getAuthentication(); // Chứa thông tin User đang đăng nhập hiện tại

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        User userFind = userService.getUserByName(authentication.getName());
        Teacher teacherLogin = iTeacherService.getInfoTeacher(userFind);
        if (teacherLogin == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return new ResponseEntity<>(teacherLogin, HttpStatus.OK);
    }

}

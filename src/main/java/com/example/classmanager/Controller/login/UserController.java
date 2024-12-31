package com.example.classmanager.Controller.login;

import com.example.classmanager.Entity.response.ApiResponse;
import com.example.classmanager.Entity.response.UserResponse;
import com.example.classmanager.Model.Teacher;
import com.example.classmanager.Model.login.User;
import com.example.classmanager.Service.Teacher.ITeacherService;
import com.example.classmanager.Service.login.IUserService;
import com.example.classmanager.dto.dto.AccountDto;
import com.example.classmanager.dto.dto.UserCreateDto;
import com.example.classmanager.dto.projection.UserProjection;
import com.example.classmanager.exception.AppException;
import com.example.classmanager.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<ApiResponse<String>> createNewUser(@RequestBody AccountDto request) {
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
    @GetMapping("/all-user")
    public ResponseEntity<Page<UserProjection>> pageGetAllUser(@RequestParam(required = false, defaultValue = "") String username,
                                                               @PageableDefault(size = 5, page = 0) Pageable pageable,
                                                               @RequestParam(required = false, defaultValue = "username") String sort) {
        Sort sort1 = Sort.by(Sort.Direction.ASC, sort);
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort1);
        Page<UserProjection> userProjections = userService.pageGetAllUser(username, "ADMIN", pageableWithSort);
        return new ResponseEntity<>(userProjections, HttpStatus.OK);
    }
    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>(ApiResponse.<String>builder()
                    .message("Delete Success")
                    .build(), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.<String>builder()
                    .message("Delete Failed")
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<UserProjection> checkUsername(@PathVariable String username) {
        UserProjection userProjection = userService.checkUsername(username);
        if (userProjection == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userProjection, HttpStatus.OK);
    }
}

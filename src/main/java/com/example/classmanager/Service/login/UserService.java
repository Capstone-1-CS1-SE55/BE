package com.example.classmanager.Service.login;

import com.example.classmanager.Entity.response.UserResponse;
import com.example.classmanager.Model.Student;
import com.example.classmanager.Model.Teacher;
import com.example.classmanager.Model.login.User;
import com.example.classmanager.Repository.IStudentRepository;
import com.example.classmanager.Repository.ITeacherRepository;
import com.example.classmanager.Repository.login.UserRepository;
import com.example.classmanager.dto.UserCreateDto;
import com.example.classmanager.enums.Role;
import com.example.classmanager.exception.AppException;
import com.example.classmanager.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService implements IUserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ITeacherRepository iTeacherRepository;

    @Autowired
    IStudentRepository iStudentRepository;

    @Override
    @Transactional
    public void createUserTeacher(UserCreateDto userStudentDto) {
        HashSet<String> role = new HashSet<>();
        role.add(Role.TEACHER.name());
        User user = User.builder()
                .username(userStudentDto.getUsername())
                .password(passwordEncoder.encode(userStudentDto.getPassword()))
                .roles(role).build();
        User savedUser = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setTeacherName(userStudentDto.getName());
        teacher.setBirthday(userStudentDto.getBirthday());
        teacher.setEmail(userStudentDto.getEmail());
        teacher.setUser(savedUser);
        iTeacherRepository.save(teacher);
    }

    @Override
    @Transactional
    public void createUserStudent(UserCreateDto userStudentDto) {
        HashSet<String> role = new HashSet<>();
        role.add(Role.STUDENT.name());
        User user = User.builder()
                .username(userStudentDto.getUsername())
                .password(passwordEncoder.encode(userStudentDto.getPassword()))
                .roles(role).build();
        User savedUser = userRepository.save(user);

        Student student = new Student();
        student.setStudentName(userStudentDto.getName());
        student.setBirthday(userStudentDto.getBirthday());
        student.setEmail(userStudentDto.getEmail());
        student.setUser(savedUser);
        iStudentRepository.save(student);
    }

    @Override
    public boolean existUserByName(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return true;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        log.info("In method get users");
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name") // Nếu đăng nhập có username trùng nhau thì chạy
    public UserResponse getUser(Long id) {
        UserResponse userResponse = new UserResponse();
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public User getUserByName(String userName) {
        User userFind = userRepository.findUserByUsername(userName);
        if (userFind == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return userFind;
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getMyInfo() {
        UserResponse userResponse = new UserResponse();
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }
}

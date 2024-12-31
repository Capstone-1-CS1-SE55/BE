package com.example.classmanager.Service.login;

import com.example.classmanager.Entity.response.UserResponse;
import com.example.classmanager.Model.Student;
import com.example.classmanager.Model.Teacher;
import com.example.classmanager.Model.login.User;
import com.example.classmanager.Repository.IStudentRepository;
import com.example.classmanager.Repository.ITeacherRepository;
import com.example.classmanager.Repository.login.UserRepository;
import com.example.classmanager.dto.dto.AccountDto;
import com.example.classmanager.dto.dto.UserCreateDto;
import com.example.classmanager.dto.projection.RoleProjection;
import com.example.classmanager.dto.projection.UserProjection;
import com.example.classmanager.enums.Role;
import com.example.classmanager.exception.AppException;
import com.example.classmanager.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;


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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void createUserTeacher(AccountDto accountDto) {
        HashSet<String> role = new HashSet<>();
        role.add(Role.TEACHER.name());
        User user = User.builder()
                .username(accountDto.getUsername())
                .password(passwordEncoder.encode(accountDto.getPassword()))
                .isDelete(Boolean.FALSE)
                .roles(role).build();
        User savedUser = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setTeacherName(accountDto.getName());
        teacher.setBirthday(accountDto.getBirthday());
        teacher.setEmail(accountDto.getEmail());
        teacher.setUser(savedUser);
        teacher.setGender(accountDto.getGender());
        teacher.setPhoneNumber(accountDto.getPhone());
        iTeacherRepository.save(teacher);
    }

    @Override
    @Transactional
    public void createUserStudent(AccountDto accountDto) {
        HashSet<String> role = new HashSet<>();
        role.add(Role.STUDENT.name());
        User user = User.builder()
                .username(accountDto.getUsername())
                .password(passwordEncoder.encode(accountDto.getPassword()))
                .roles(role).build();
        User savedUser = userRepository.save(user);

        Student student = new Student();
        student.setStudentName(accountDto.getName());
        student.setBirthday(accountDto.getBirthday());
        student.setEmail(accountDto.getEmail());
        student.setUser(savedUser);
        student.setGender(accountDto.getGender());
        student.setPhoneNumber(accountDto.getPhone());
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
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserProjection> pageGetAllUser(String username, String excludedRole, Pageable pageable) {
        return userRepository.pageGetAllUser("%" + username + "%", excludedRole, pageable);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long userId) {
        try {
            RoleProjection roleProjection = userRepository.getRole(userId);
            User user = userRepository.findById(userId).orElse(null);
            if(Objects.equals(roleProjection.getRole(), "TEACHER")) {
                String callProcedure = "CALL delete_account_with_role_teacher(?);";
                jdbcTemplate.update(callProcedure, userId);
            } else if(Objects.equals(roleProjection.getRole(), "STUDENT")) {
                String callProcedure = "{CALL delete_account_with_role_student(?)}";
                jdbcTemplate.update(callProcedure, userId);
            }
        } catch (Exception e) {
            System.out.println("Error occurred during stored procedure execution: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public UserProjection checkUsername(String username) {
        return userRepository.checkUsername(username);
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

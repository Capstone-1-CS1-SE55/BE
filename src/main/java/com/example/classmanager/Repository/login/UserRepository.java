package com.example.classmanager.Repository.login;

import com.example.classmanager.Model.login.User;
import com.example.classmanager.dto.projection.RoleProjection;
import com.example.classmanager.dto.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
     Optional<User> findByUsername(String username);
    User findUserByUsername(String username);
    @Query("select u.userId as userId, u.username as username, u.roles as role, t.teacherName as teacherName, " +
            "s.studentName as studentName, t.email as teacherEmail, s.email as studentEmail, t.gender as teacherGender, " +
            "s.gender as studentGender, t.phoneNumber as teacherPhone, s.phoneNumber as studentPhone, " +
            "t.birthday as teacherBirthday, s.birthday as studentBirthday from User u left join Student s on u.userId = s.user.userId left join Teacher t " +
            "on u.userId = t.user.userId where u.username like :username and :excludedRole not member of u.roles")
    Page<UserProjection> pageGetAllUser(@Param("username") String username, @Param("excludedRole") String excludedRole, Pageable pageable);
    @Query("select u.roles as role from User u where u.userId = :userId")
    RoleProjection getRole(@Param("userId") Long userId);
    @Query("select u.username as username from User u where u.username = :username")
    UserProjection checkUsername(@Param("username") String username);
}

package com.example.classmanager.Repository.login;

import com.example.classmanager.Model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
     Optional<User> findByUsername(String username);
    User findUserByUsername(String username);
}

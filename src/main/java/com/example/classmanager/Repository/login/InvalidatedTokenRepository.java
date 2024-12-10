package com.example.classmanager.Repository.login;

import com.example.classmanager.Model.login.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}

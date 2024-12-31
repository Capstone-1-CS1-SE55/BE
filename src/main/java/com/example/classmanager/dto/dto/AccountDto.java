package com.example.classmanager.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String username;
    private String password;
    private String role;
    private String email;
    private String name;
    private LocalDate birthday;
    private String gender;
    private String phone;
}

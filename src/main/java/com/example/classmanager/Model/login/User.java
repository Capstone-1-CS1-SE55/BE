package com.example.classmanager.Model.login;


import com.example.classmanager.Model.Question;
import com.example.classmanager.Model.Student;
import com.example.classmanager.Model.Teacher;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    String username;

    String password;

    @ElementCollection
    Set<String> roles;
}

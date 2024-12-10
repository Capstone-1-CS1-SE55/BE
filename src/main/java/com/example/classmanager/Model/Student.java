package com.example.classmanager.Model;

import com.example.classmanager.Model.login.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    private String studentName;

    private String email;

    @Column(columnDefinition = "DATE")
    private LocalDate birthday;

    private String gender;

    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

//    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<ClassroomStudent> classroomStudents;
}

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

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String studentName;

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String email;

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate birthday;

    @Column(columnDefinition = "NVARCHAR(10)", nullable = false)
    private String gender;

    @Column(columnDefinition = "NVARCHAR(15)", nullable = false)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

//    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<ClassroomStudent> classroomStudents;
}

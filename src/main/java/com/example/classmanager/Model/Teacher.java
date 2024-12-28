package com.example.classmanager.Model;

import com.example.classmanager.Model.login.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String teacherName;

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

//    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<Classroom> classrooms;
}
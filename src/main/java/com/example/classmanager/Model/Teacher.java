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

    private String teacherName;

    private String email;

    @Column(columnDefinition = "DATE")
    private LocalDate birthday;

    private String gender;

    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

//    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<Classroom> classrooms;
}
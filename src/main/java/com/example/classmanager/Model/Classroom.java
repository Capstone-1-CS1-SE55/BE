package com.example.classmanager.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    private Long classroomId;

    private String classroomName;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private Integer quantity = 0;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
    private Teacher teacher;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

//    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<ClassroomStudent> classroomStudents;
}

package com.example.classmanager.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(columnDefinition = "DATE")
    private LocalDate enrollmentDate;

    @Column(nullable = false)
    private String status = "Chưa tham gia";

    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id")
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_Id")
    private Student student;

    @PrePersist
    protected void onCreate() {
        this.enrollmentDate = LocalDate.now();
    }
}

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

    @Column(name = "enrollment_date", nullable = false, columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
    private LocalDate enrollmentDate;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String status = "Đã tham gia";

    @Column(name = "is_delete", nullable = false, columnDefinition = "BOOLEAN DEFAULT 0")
    private Boolean isDelete=Boolean.FALSE;

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

package com.example.classmanager.Model;

import com.example.classmanager.Entity.StudentAssignmentId;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Check(constraints = "grade >= 0 AND grade <= 10")
public class StudentAssignment {

    @EmbeddedId
    private StudentAssignmentId id;

    @Column(name = "submission_date", columnDefinition = "DATETIME")
    private LocalDateTime submissionDate;

    @Column(precision = 4, scale = 2) // DECIMAL(3, 2)
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "10.0", inclusive = true)
    private BigDecimal grade;

    @Column(name = "status", nullable = false, columnDefinition = "NVARCHAR(50) DEFAULT 'Chưa làm'")
    private String status = "Chưa làm";

    @Column(name = "time_to_work", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime timeToWork;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("assignmentId")
    @JoinColumn(name = "assignment_id", referencedColumnName = "assignment_id")
    private Assignment assignment;

    @PrePersist
    protected void onCreate() {
        this.timeToWork = LocalDateTime.now();
    }
}

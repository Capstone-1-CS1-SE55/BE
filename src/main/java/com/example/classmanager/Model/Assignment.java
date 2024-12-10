package com.example.classmanager.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", columnDefinition = "DATETIME")
    private LocalDateTime startDate;

    @Column(name = "due_date", columnDefinition = "DATETIME")
    private LocalDateTime dueDate; //hạn nộp

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(50) DEFAULT 'Đã giao'")
    private String status = "Đã giao";

    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id")
    private Classroom classroom;

    // Đảm bảo trường createdDate có giá trị là ngày hiện tại khi bản ghi được tạo
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}

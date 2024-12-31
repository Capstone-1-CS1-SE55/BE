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

    @Column(columnDefinition = "NVARCHAR(255)")
    private String classroomName;

    @Column(name = "created_date", nullable = false, columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
    private LocalDate createdDate;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Integer quantity = 0;

    @Column(name = "is_delete", nullable = false, columnDefinition = "BOOLEAN DEFAULT 0")
    private Boolean isDelete = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id", nullable = false)
    private Teacher teacher;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }

//    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<ClassroomStudent> classroomStudents;
}

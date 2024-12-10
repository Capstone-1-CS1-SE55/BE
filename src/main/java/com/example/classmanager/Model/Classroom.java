package com.example.classmanager.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


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
    private LocalDate createdDate;

    @Column(nullable = false)
    private Integer quantity = 0;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
    private Teacher teacher;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }

//    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<ClassroomStudent> classroomStudents;
}

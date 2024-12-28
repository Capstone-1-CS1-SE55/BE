package com.example.classmanager.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Check(constraints = "max_score >= 0 AND max_score <= 10")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "question_text", columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Column(precision = 4, scale = 2, name = "max_score", nullable = false) // DECIMAL(3, 2)
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "10.0", inclusive = true)
    private BigDecimal maxScore;

    @Column(name = "is_delete", nullable = false, columnDefinition = "BOOLEAN DEFAULT 0")
    private Boolean isDelete=Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "assignment_id", referencedColumnName = "assignment_id", nullable = false)
    private Assignment assignment;
}

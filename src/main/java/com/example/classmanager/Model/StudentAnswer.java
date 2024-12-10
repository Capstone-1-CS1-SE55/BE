package com.example.classmanager.Model;

import com.example.classmanager.Entity.StudentAnswerId;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Check(constraints = "score >= 0 AND score <= 10")
public class StudentAnswer {

    @EmbeddedId
    private StudentAnswerId id;

    @Column(name = "answer_text", columnDefinition = "TEXT")
    private String answerText;

    @Column(precision = 4, scale = 2) // DECIMAL(3, 2)
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "10.0", inclusive = true)
    private BigDecimal score;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    private Question question;
}

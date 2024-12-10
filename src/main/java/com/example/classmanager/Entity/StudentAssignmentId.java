package com.example.classmanager.Entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentAssignmentId implements Serializable {
    private Long studentId;

    private Long assignmentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentAssignmentId that = (StudentAssignmentId) o;
        return Objects.equals(studentId, that.studentId) &&
                Objects.equals(assignmentId, that.assignmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, assignmentId);
    }
}

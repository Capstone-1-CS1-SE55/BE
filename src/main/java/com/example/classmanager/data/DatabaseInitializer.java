package com.example.classmanager.data;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        try {
            String sql = "CREATE PROCEDURE delete_class_with_relations(IN classId BIGINT)\n" +
                    "BEGIN\n" +
                    "   DECLARE exitHandler INT DEFAULT 0;\n" +
                    "   DECLARE CONTINUE HANDLER FOR SQLEXCEPTION\n" +
                    "   BEGIN\n" +
                    "       SET exitHandler = 1;\n" +
                    "       ROLLBACK;\n" +
                    "   END;\n" +
                    "   START TRANSACTION;\n" +
                    "   DELETE FROM teacher_answer WHERE question_id IN (select question_id FROM question " +
                    "WHERE assignment_id IN (SELECT assignment_id FROM assignment WHERE classroom_id = classId));\n" +
                    "   DELETE FROM student_answer WHERE question_id IN (SELECT question_id FROM question " +
                    "WHERE assignment_id IN (SELECT assignment_id FROM assignment WHERE classroom_id = classId));\n" +
                    "   DELETE FROM question WHERE assignment_id IN (SELECT assignment_id FROM assignment " +
                    "WHERE classroom_id = classId);\n" +
                    "   DELETE FROM student_assignment WHERE assignment_id IN " +
                    "(SELECT assignment_id FROM assignment WHERE classroom_id = classId);\n" +
                    "   DELETE FROM classroom_student WHERE classroom_id = classId;\n" +
                    "   DELETE FROM assignment WHERE classroom_id = classId;\n" +
                    "   DELETE FROM classroom WHERE classroom_id = classId;\n" +
                    "   IF exitHandler = 0 THEN\n" +
                    "       COMMIT;\n" +
                    "   END IF;\n" +
                    "END;";

            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @PostConstruct
    public void deleteAssignment() {
        try {
            String sqlDeleteAssignment = "CREATE PROCEDURE delete_assignment_with_relations(IN assignmentId BIGINT)\n" +
                    "BEGIN\n" +
                    "   DECLARE exitHandler INT DEFAULT 0;\n" +
                    "   DECLARE CONTINUE HANDLER FOR SQLEXCEPTION\n" +
                    "   BEGIN\n" +
                    "       SET exitHandler = 1;\n" +
                    "       ROLLBACK;\n" +
                    "   END;\n" +
                    "   START TRANSACTION;\n" +
                    "   DELETE FROM teacher_answer WHERE question_id IN (SELECT question_id FROM question " +
                    "WHERE assignment_id = assignmentId);\n" +
                    "   DELETE FROM student_answer WHERE question_id IN (SELECT question_id FROM question " +
                    "WHERE assignment_id = assignmentId);\n" +
                    "   DELETE FROM question WHERE assignment_id = assignmentId;\n" +
                    "   DELETE FROM student_assignment WHERE assignment_id = assignmentId;\n" +
                    "   DELETE FROM assignment WHERE assignment_id = assignmentId;\n" +
                    "   IF exitHandler = 0 THEN\n" +
                    "       COMMIT;\n" +
                    "   END IF;\n" +
                    "END;";
            jdbcTemplate.execute(sqlDeleteAssignment);
        }catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @PostConstruct
    public void deleteQuestion() {
        try {
            String sqlDeleteQuestion = "CREATE PROCEDURE delete_question_with_relations(IN questionId BIGINT)\n" +
                    "BEGIN\n" +
                    "   DECLARE exitHandler INT DEFAULT 0;\n" +
                    "   DECLARE CONTINUE HANDLER FOR SQLEXCEPTION\n" +
                    "   BEGIN\n" +
                    "       SET exitHandler = 1;\n" +
                    "       ROLLBACK;\n" +
                    "   END;\n" +
                    "   START TRANSACTION;\n" +
                    "   DELETE FROM teacher_answer WHERE question_id = questionId;\n" +
                    "   DELETE FROM student_answer WHERE question_id = questionId;\n" +
                    "   DELETE FROM question WHERE question_id = questionId;\n" +
                    "   IF exitHandler = 0 THEN\n" +
                    "       COMMIT;\n" +
                    "   END IF;\n" +
                    "END;";
            jdbcTemplate.execute(sqlDeleteQuestion);
        }catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}

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

    @PostConstruct
    public void createDeleteAccountProcedure() {
        try {
            String sqlCreateProcedure = "CREATE PROCEDURE delete_account_with_role_teacher(IN userId BIGINT)\n" +
                    "BEGIN\n" +
                    "   DECLARE exitHandler INT DEFAULT 0;\n" +
                    "   DECLARE CONTINUE HANDLER FOR SQLEXCEPTION\n" +
                    "   BEGIN\n" +
                    "       SET exitHandler = 1;\n" +
                    "       ROLLBACK;\n" +
                    "   END;\n" +
                    "   START TRANSACTION;\n" +
                    "   DELETE FROM teacher_answer ta WHERE ta.question_id IN (SELECT q.question_id FROM question q \n" +
                    "       JOIN assignment a ON q.assignment_id = a.assignment_id \n" +
                    "       JOIN classroom c ON c.classroom_id = a.classroom_id \n" +
                    "       JOIN teacher t ON t.teacher_id = c.teacher_id \n" +
                    "       JOIN user u ON u.user_id = t.user_id WHERE u.user_id = userId);\n" +
                    "   DELETE FROM student_answer sa WHERE sa.question_id IN (SELECT q.question_id FROM question q \n" +
                    "       JOIN assignment a ON q.assignment_id = a.assignment_id \n" +
                    "       JOIN classroom c ON c.classroom_id = a.classroom_id \n" +
                    "       JOIN teacher t ON t.teacher_id = c.teacher_id \n" +
                    "       JOIN user u ON u.user_id = t.user_id WHERE u.user_id = userId);\n" +
                    "   DELETE FROM question q WHERE q.assignment_id IN (SELECT a.assignment_id FROM assignment a \n" +
                    "       JOIN classroom c ON c.classroom_id = a.classroom_id \n" +
                    "       JOIN teacher t ON t.teacher_id = c.teacher_id \n" +
                    "       JOIN user u ON u.user_id = t.user_id WHERE u.user_id = userId);\n" +
                    "   DELETE FROM student_assignment sa WHERE sa.assignment_id IN (SELECT a.assignment_id FROM assignment a \n" +
                    "       JOIN classroom c ON c.classroom_id = a.classroom_id \n" +
                    "       JOIN teacher t ON t.teacher_id = c.teacher_id \n" +
                    "       JOIN user u ON u.user_id = t.user_id WHERE u.user_id = userId);\n" +
                    "   DELETE FROM assignment a WHERE a.classroom_id IN (SELECT c.classroom_id FROM classroom c \n" +
                    "       JOIN teacher t ON t.teacher_id = c.teacher_id \n" +
                    "       JOIN user u ON u.user_id = t.user_id WHERE u.user_id = userId);\n" +
                    "CREATE TEMPORARY TABLE temp_classroom_ids AS\n" +
                    "SELECT c.classroom_id\n" +
                    "FROM classroom c\n" +
                    "JOIN teacher t ON t.teacher_id = c.teacher_id\n" +
                    "JOIN user u ON u.user_id = t.user_id\n" +
                    "WHERE u.user_id = userId;" +
                    "   DELETE FROM classroom_student ct WHERE ct.classroom_id IN (SELECT classroom_id FROM temp_classroom_ids);\n" +
                    "DROP TEMPORARY TABLE temp_classroom_ids;" +
                    "   DELETE FROM notification n WHERE n.classroom_id IN (SELECT c.classroom_id FROM classroom c \n" +
                    "       JOIN teacher t ON t.teacher_id = c.teacher_id \n" +
                    "       JOIN user u ON u.user_id = t.user_id WHERE u.user_id = userId);\n" +
                    "   DELETE FROM classroom c WHERE c.teacher_id IN (SELECT t.teacher_id FROM teacher t \n" +
                    "       JOIN user u ON u.user_id = t.user_id WHERE u.user_id = userId);\n" +
                    "   DELETE FROM teacher t WHERE t.user_id IN (SELECT u.user_id FROM user u WHERE u.user_id = userId);\n" +
                    "   DELETE FROM user_roles ur WHERE ur.user_user_id = userId;\n" +
                    "   DELETE FROM user u WHERE u.user_id = userId;\n" +
                    "   IF exitHandler = 0 THEN\n" +
                    "       COMMIT;\n" +
                    "   END IF;\n" +
                    "END;";
            jdbcTemplate.execute(sqlCreateProcedure);
        } catch (DataAccessException e) {
            System.err.println("Error creating procedure: " + e.getMessage());
        }
    }


    @PostConstruct
    public void deleteAccountStudent() {
        try {
            String sqlDeleteQuestion = "CREATE PROCEDURE delete_account_with_role_student(IN userId BIGINT)\n" +
                    "BEGIN\n" +
                    "   DECLARE exitHandler INT DEFAULT 0;\n" +
                    "   DECLARE CONTINUE HANDLER FOR SQLEXCEPTION\n" +
                    "   BEGIN\n" +
                    "       SET exitHandler = 1;\n" +
                    "       ROLLBACK;\n" +
                    "   END;\n" +
                    "   START TRANSACTION;\n" +
                    "   DELETE FROM student_answer sa WHERE sa.student_id in (select s.student_id from student s join " +
                    "user u on u.user_id = s.user_id where u.user_id = userId); \n" +
                    "   DELETE FROM student_assignment sa WHERE sa.assignment_id in (select a.assignment_id from assignment a join classroom c on c.classroom_id = a.classroom_id " +
                    "join classroom_student ct on ct.classroom_id = c.classroom_id join student s on s.student_id = ct. student_id " +
                    "join user u on u.user_id = s.user_id where u.user_id = userId); \n" +
                    "   DELETE FROM classroom_student WHERE student_id in (select s.student_id from student s " +
                    "join user u on u.user_id = s.user_id where u.user_id = userId); \n" +
                    "   DELETE FROM notification WHERE student_id in (select s.student_id from student s " +
                    "join user u on u.user_id = s.user_id where u.user_id = userId); \n" +
                    "   DELETE FROM student s WHERE s.user_id in (select u.user_id from user u where u.user_id = userId); \n" +
                    "   DELETE FROM user_roles ur WHERE ur.user_user_id = userId; \n" +
                    "   DELETE FROM user u WHERE u.user_id = userId; \n" +
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

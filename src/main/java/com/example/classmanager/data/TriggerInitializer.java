package com.example.classmanager.data;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TriggerInitializer {

    private final SessionFactory sessionFactory;

    @Autowired
    public TriggerInitializer(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createTrigger() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("CREATE TRIGGER update_student_count " +
                            "AFTER INSERT ON classroom_student " +
                            "FOR EACH ROW " +
                            "BEGIN " +
                            "   UPDATE classroom " +
                            "   SET quantity = quantity + 1 " +
                            "   WHERE classroom_id = NEW.classroom_id; " +
                            "END;")
                    .executeUpdate();
            session.getTransaction().commit();
            System.out.println("Trigger created successfully.");
        } catch (Exception e) {
            System.out.println("Error creating trigger: " + e.getMessage());
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void deleteStudentFromClassroomTrigger() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("CREATE TRIGGER decrease_classroom_quantity " +
                            "AFTER DELETE ON classroom_student " +
                            "FOR EACH ROW " +
                            "BEGIN " +
                            "   UPDATE classroom " +
                            "   SET quantity = quantity - 1 " +
                            "   WHERE classroom_id = OLD.classroom_id; " +
                            "END;")
                    .executeUpdate();
            session.getTransaction().commit();
            System.out.println("Trigger created successfully.");
        } catch (Exception e) {
            System.out.println("Error creating trigger: " + e.getMessage());
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addStudentToAssignment() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("CREATE TRIGGER trg_after_assignment_insert \n" +
                            "AFTER INSERT ON assignment \n" +
                            "FOR EACH ROW \n" +
                            "BEGIN \n" +
                            "   INSERT INTO student_assignment (student_id, assignment_id, time_to_work) \n" +
                            "   SELECT cs.student_id, NEW.assignment_id, TIMEDIFF(NEW.due_date, NEW.start_date) \n" +
                            "   FROM classroom_student cs \n" +
                            "   WHERE cs.classroom_id = NEW.classroom_id \n" +
                            "   AND cs.status = 'Đã tham gia';" +
                            "END;")
                    .executeUpdate();
            session.getTransaction().commit();
            System.out.println("Trigger created successfully.");
        } catch (Exception e) {
            System.out.println("Error creating trigger: " + e.getMessage());
        }
    }
}

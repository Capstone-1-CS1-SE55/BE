package com.example.classmanager.Service.Classroom;

import com.example.classmanager.Model.Classroom;
import com.example.classmanager.Model.ClassroomStudent;
import com.example.classmanager.Model.Student;
import com.example.classmanager.Repository.IClassroomRepository;
import com.example.classmanager.Repository.IClassroomStudentRepository;
import com.example.classmanager.Repository.IStudentRepository;
import com.example.classmanager.dto.ClassroomProjection;
import com.example.classmanager.dto.ClassroomStudentProjection;
import com.example.classmanager.dto.FindAllStudentInClassroomProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class ClassroomService implements IClassroomService {
    @Autowired
    private IClassroomRepository iClassroomRepository;

    @Autowired
    private IClassroomStudentRepository iClassroomStudentRepository;

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void CreateNewClass(Classroom classroom, List<String> listEmails) {
        Classroom savedClassroom = iClassroomRepository.save(classroom);
        for (String email: listEmails) {
            if (!Pattern.matches(EMAIL_REGEX, email)) {
                return;
            }
            Student student = iStudentRepository.findStudentByEmail(email);
            if (student == null) {
                return;
            }
            ClassroomStudent classroomStudent = new ClassroomStudent();
            classroomStudent.setStudent(student);
            classroomStudent.setClassroom(savedClassroom);
            iClassroomStudentRepository.save(classroomStudent);
        }
    }

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    public Boolean existsClassroomNameByTeacher(Long teacherId, String ClassroomName) {
        return iClassroomRepository.existsClassroomNameByTeacher(teacherId, ClassroomName);
    }

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    public void deleteClassroom(Long id) {
        try {
            String callProcedure = "{CALL delete_class_with_relations(?)}";
            jdbcTemplate.update(callProcedure, id);
        } catch (Exception e) {
            System.out.println("Error occurred during stored procedure execution: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public Classroom getClassroomById(Long id) {
        return iClassroomRepository.findById(id).orElse(null);
    }

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    public void updateClassroomNameById(Long id, String name) {
        iClassroomRepository.updateClassroomNameById(id, name);
    }

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    public List<ClassroomStudentProjection> getStudentInClassroom(Long id) {
        return iClassroomRepository.getStudentInClassroom(id);
    }

    @Override
    public List<ClassroomStudentProjection> getStudentInClassroomByStudentName(Long id, String studentName) {
        return iClassroomRepository.getStudentInClassroomByStudentName(id, "%" + studentName + "%");
    }

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    public List<ClassroomProjection> getClassroomsByTeacherUsername(String username) {
        return iClassroomRepository.getClassroomsByTeacherUsername(username);
    }

    @Override
    public void addStudentToClass(ClassroomStudent classroomStudent) {
        iClassroomStudentRepository.save(classroomStudent);
    }

    @Override
    public Boolean existsStudentInClassroom(Long studentId, Long classroomId) {
        return iClassroomStudentRepository.existsStudentInClassroom(studentId, classroomId);
    }

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    public void deleteStudentFromClassroom(Long studentId, Long classroomId) {
        iClassroomRepository.deleteStudentFromClassroom(studentId, classroomId);
    }

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    public Page<ClassroomProjection> PagegetClassroomsByTeacherId(String username, String classroomName, Pageable pageable) {
        return iClassroomRepository.PagegetClassroomsByTeacherId(username, classroomName, pageable);
    }

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    public Page<FindAllStudentInClassroomProjection> PageGetStudentInClassroomByStudentName(Long classroomId, String studentName, Pageable pageable) {
        return iClassroomRepository.PageGetStudentInClassroomByStudentName(classroomId, studentName, pageable);
    }
}

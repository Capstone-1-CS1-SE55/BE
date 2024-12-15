package com.example.classmanager.Service.Classroom;

import com.example.classmanager.Model.Classroom;
import com.example.classmanager.Model.ClassroomStudent;
import com.example.classmanager.dto.projection.ClassroomProjection;
import com.example.classmanager.dto.projection.ClassroomStudentProjection;
import com.example.classmanager.dto.projection.FindAllStudentInClassroomProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClassroomService {
    void CreateNewClass(Classroom classroom, List<String> listEmails);
    Boolean existsClassroomNameByTeacher(Long teacherId, String ClassroomName);
    void deleteClassroom(Long id);
    Classroom getClassroomById(Long id);
    void updateClassroomNameById(Long id, String name);
    List<ClassroomStudentProjection> getStudentInClassroom(Long id);
    List<ClassroomStudentProjection> getStudentInClassroomByStudentName(Long id, String studentName);
    List<ClassroomProjection> getClassroomsByTeacherUsername(String username);
    void addStudentToClass(ClassroomStudent classroomStudent);
    Boolean existsStudentInClassroom(Long studentId, Long classroomId);
    void deleteStudentFromClassroom(Long studentId, Long classroomId);
    Page<ClassroomProjection> PagegetClassroomsByTeacherId(String username, String classroomName, Pageable pageable);
    Page<FindAllStudentInClassroomProjection> PageGetStudentInClassroomByStudentName(Long classroomId, String studentName, Pageable pageable);
}

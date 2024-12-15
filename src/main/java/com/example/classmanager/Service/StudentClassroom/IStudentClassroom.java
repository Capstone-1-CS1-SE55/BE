package com.example.classmanager.Service.StudentClassroom;

import com.example.classmanager.dto.projection.StudentProjection;
import com.example.classmanager.dto.projection.FindAllClassOfStudentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IStudentClassroom {
    Page<FindAllClassOfStudentProjection> PageGetAllClassOfStudent(String classroomName, Pageable pageable);
    Page<StudentProjection> findStudentByClassroomId(Long classroomId, Pageable pageable);
}

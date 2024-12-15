package com.example.classmanager.Service.StudentClassroom;

import com.example.classmanager.Repository.IClassroomRepository;
import com.example.classmanager.Repository.IClassroomStudentRepository;
import com.example.classmanager.Repository.IStudentRepository;
import com.example.classmanager.dto.projection.StudentProjection;
import com.example.classmanager.dto.projection.FindAllClassOfStudentProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StudentClassroom implements IStudentClassroom{
    @Autowired
    private IClassroomStudentRepository iClassroomStudentRepository;

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IClassroomRepository iClassroomRepository;
    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public Page<FindAllClassOfStudentProjection> PageGetAllClassOfStudent(String classroomName, Pageable pageable) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return iClassroomStudentRepository.PageGetAllClassOfStudent(authentication.getName(), classroomName, pageable);
    }

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public Page<StudentProjection> findStudentByClassroomId(Long classroomId, Pageable pageable) {
        return iClassroomStudentRepository.findStudentByClassroomId(classroomId, pageable);
    }
}

package com.example.classmanager.Service.Teacher;

import com.example.classmanager.Model.Teacher;
import com.example.classmanager.Model.login.User;
import com.example.classmanager.Repository.ITeacherRepository;
import com.example.classmanager.exception.AppException;
import com.example.classmanager.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TeacherService implements ITeacherService{
    @Autowired
    private ITeacherRepository iTeacherRepository;

    @Override
    public Teacher getTeacherById(Long teacherId) {
        return iTeacherRepository.findById(teacherId).orElse(null);
    }

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    public Teacher getInfoTeacher(User user) {
        Teacher teacherFind = iTeacherRepository.findTeacherByUser( user);
        if (teacherFind == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return teacherFind;
    }

    @Override
    @PreAuthorize("hasRole('TEACHER')")
    public Long findTeacherIdByUsername(String username) {
        return iTeacherRepository.findTeacherIdByUsername(username);
    }
}

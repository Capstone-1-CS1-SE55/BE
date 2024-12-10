package com.example.classmanager.Service.Student;

import com.example.classmanager.Model.Student;
import com.example.classmanager.Repository.IStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService implements IStudentService{
    @Autowired
    private IStudentRepository iStudentRepository;

    @Override
    public Student findStudentByEmail(String email) {
        return iStudentRepository.findStudentByEmail(email);
    }

    @Override
    public Student getStudentById(Long id) {
        return iStudentRepository.findById(id).orElse(null);
    }
}

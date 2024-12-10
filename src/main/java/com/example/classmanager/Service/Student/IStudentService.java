package com.example.classmanager.Service.Student;

import com.example.classmanager.Model.Student;

public interface IStudentService {
    Student findStudentByEmail(String email);
    Student getStudentById(Long id);
}

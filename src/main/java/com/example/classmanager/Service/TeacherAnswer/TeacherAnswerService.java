package com.example.classmanager.Service.TeacherAnswer;

import com.example.classmanager.Repository.ITeacherAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherAnswerService implements ITeacherAnswerService{
    @Autowired
    private ITeacherAnswerRepository iTeacherAnswerRepository;
}

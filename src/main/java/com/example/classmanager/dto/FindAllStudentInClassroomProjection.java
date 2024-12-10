package com.example.classmanager.dto;

import java.time.LocalDate;

public interface FindAllStudentInClassroomProjection {
    Long getClassroomId();

    String getClassroomName();

    Long getStudentId();

    String getStudentName();

    String getEmail();

    LocalDate getBirthday();

    String getGender();

    String getPhoneNumber();
}

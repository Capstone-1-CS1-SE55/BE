package com.example.classmanager.dto.projection;

import java.time.LocalDate;
public interface StudentProjection {
    Long getStudentId();
    String getStudentName();
    String getEmail();
    LocalDate getBirthday();
    String getGender();
    String getStatus();
}

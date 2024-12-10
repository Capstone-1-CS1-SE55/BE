package com.example.classmanager.Controller;

import com.example.classmanager.Entity.response.ApiResponse;
import com.example.classmanager.Model.Student;
import com.example.classmanager.Service.Student.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private IStudentService iStudentService;

    @GetMapping("/get-student-by-email")
    public ResponseEntity<ApiResponse<Student>> getStudentByEmail(@RequestParam(required = false, defaultValue = "") String email) {
        Student student = iStudentService.findStudentByEmail(email);
        if(student == null) {
            return new ResponseEntity<>(ApiResponse.<Student>builder()
                    .message("student does not exist")
                    .build(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ApiResponse.<Student>builder()
                .result(student)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getClassroomById(@PathVariable("id") Long id) {
        Student student = iStudentService.getStudentById(id);
        if (student == null) {
            return new ResponseEntity<>(ApiResponse.<Student>builder()
                    .message("Student does not exist")
                    .build(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ApiResponse.<Student>builder()
                .result(student)
                .build(), HttpStatus.OK);
    }
}

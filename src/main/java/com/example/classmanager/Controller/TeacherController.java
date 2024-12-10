package com.example.classmanager.Controller;

import com.example.classmanager.Entity.response.ApiResponse;
import com.example.classmanager.Model.Classroom;
import com.example.classmanager.Model.Teacher;
import com.example.classmanager.Service.Classroom.IClassroomService;
import com.example.classmanager.Service.Teacher.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private IClassroomService iClassroomService;

    @Autowired
    private ITeacherService iTeacherService;

    @PostMapping("/create-new-classroom")
    public ResponseEntity<ApiResponse<Classroom>> createNewClass(@RequestParam String name,
                                                                 @RequestParam String emails) {

        var authentication = SecurityContextHolder.getContext().getAuthentication(); // Chứa thông tin User đang đăng nhập hiện tại
        Long teacherId = iTeacherService.findTeacherIdByUsername(authentication.getName());
        Boolean exists = iClassroomService.existsClassroomNameByTeacher(teacherId, name);
        if (exists) {
            return new ResponseEntity<>(ApiResponse.<Classroom>builder()
                    .message("class already exists")
                    .build(), HttpStatus.BAD_REQUEST);
        }
        List<String> listEmails = Arrays.asList(emails.split(","));
        System.out.println(listEmails);

        Classroom classroom = new Classroom();
        Teacher teacher = new Teacher();
        teacher.setTeacherId(teacherId);
        classroom.setClassroomName(name);
        classroom.setTeacher(teacher);
        iClassroomService.CreateNewClass(classroom, listEmails);
        return new ResponseEntity<>(ApiResponse.<Classroom>builder()
                .message("The class has been created successfully")
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Teacher>> getTeacherById(@PathVariable("id") Long id) {
        Teacher teacher = iTeacherService.getTeacherById(id);
        if (teacher == null) {
            return new ResponseEntity<>(ApiResponse.<Teacher>builder()
                    .message("teacher does not exist")
                    .build(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ApiResponse.<Teacher>builder()
                .result(teacher)
                .build(), HttpStatus.OK);
    }

}

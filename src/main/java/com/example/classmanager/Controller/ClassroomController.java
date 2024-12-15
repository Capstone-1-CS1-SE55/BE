package com.example.classmanager.Controller;

import com.example.classmanager.Entity.response.ApiResponse;
import com.example.classmanager.Model.Classroom;
import com.example.classmanager.Model.ClassroomStudent;
import com.example.classmanager.Model.Student;
import com.example.classmanager.Service.Classroom.IClassroomService;
import com.example.classmanager.Service.Student.IStudentService;
import com.example.classmanager.Service.StudentClassroom.IStudentClassroom;
import com.example.classmanager.dto.projection.StudentProjection;
import com.example.classmanager.dto.projection.ClassroomProjection;
import com.example.classmanager.dto.projection.ClassroomStudentProjection;
import com.example.classmanager.dto.projection.FindAllClassOfStudentProjection;
import com.example.classmanager.dto.projection.FindAllStudentInClassroomProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/classroom")
public class ClassroomController {
    @Autowired
    private IClassroomService iClassroomService;

    @Autowired
    private IStudentService iStudentService;

    @Autowired
    private IStudentClassroom iStudentClassroom;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Classroom>> deleteClassroom(@PathVariable("id") Long id) {
        Classroom classroom = iClassroomService.getClassroomById(id);
        if (classroom == null) {
            return new ResponseEntity<>(ApiResponse.<Classroom>builder()
                    .message("classroom does not exist")
                    .build(), HttpStatus.NOT_FOUND);
        }
        iClassroomService.deleteClassroom(id);
        return new ResponseEntity<>(ApiResponse.<Classroom>builder()
                .message("deleted successfully")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Classroom>> getClassroomById(@PathVariable("id") Long id) {
        Classroom classroom = iClassroomService.getClassroomById(id);
        if (classroom == null) {
            return new ResponseEntity<>(ApiResponse.<Classroom>builder()
                    .message("classroom does not exist")
                    .build(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ApiResponse.<Classroom>builder()
                .result(classroom)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/all-student/{id}")
    public ResponseEntity<List<ClassroomStudentProjection>> getStudentInClassroom(@PathVariable("id") Long id) {
        List<ClassroomStudentProjection> classroom = iClassroomService.getStudentInClassroom(id);
        if (classroom.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(classroom, HttpStatus.OK);
    }

    @PatchMapping("/update-classroom")
    public ResponseEntity<ApiResponse<Classroom>> updateClassroom(@RequestBody Classroom classroom) {
        Classroom classroom2 = iClassroomService.getClassroomById(classroom.getClassroomId());
        if (classroom2 == null) {
            return new ResponseEntity<>(ApiResponse.<Classroom>builder()
                    .message("classroom does not exist")
                    .build(), HttpStatus.NOT_FOUND);
        }
        iClassroomService.updateClassroomNameById(classroom.getClassroomId(), classroom.getClassroomName());
        return new ResponseEntity<>(ApiResponse.<Classroom>builder()
                .message("Updated successfully")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/student-name/{id}")
    public ResponseEntity<List<ClassroomStudentProjection>> getStudentInClassroomByStudentName(@PathVariable("id") Long id,
                                                                                               @RequestParam(required = false, defaultValue = "") String studentName) {
        List<ClassroomStudentProjection> classroom = iClassroomService.getStudentInClassroomByStudentName(id, studentName);
        if (classroom.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(classroom, HttpStatus.OK);
    }

    @GetMapping("/page-all-student-in-class/{id}")
    public ResponseEntity<Page<FindAllStudentInClassroomProjection>> PageGetStudentInClassroomByStudentName(
            @PathVariable("id") Long id,
            @RequestParam(required = false, defaultValue = "") String studentName,
            @PageableDefault(page = 0, size = 5) Pageable pageable,
            @RequestParam(required = false, defaultValue = "studentId") String sort
    ) {
        Sort sort1 = Sort.by(Sort.Direction.ASC, sort);
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort1);
        Page<FindAllStudentInClassroomProjection> list = iClassroomService.PageGetStudentInClassroomByStudentName(id, "%" + studentName + "%", pageableWithSort);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/get-all-classroom-of-teacher")
    public ResponseEntity<List<ClassroomProjection>> getClassroomsByTeacherId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        List<ClassroomProjection> classroomList = iClassroomService.getClassroomsByTeacherUsername(authentication.getName());
        if (classroomList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(classroomList, HttpStatus.OK);
    }

    @GetMapping("/page-get-all-classroom-of-teacher/{username}")
    public ResponseEntity<Page<ClassroomProjection>> findAll(@PageableDefault(page = 0, size = 5) Pageable pageable,
                                                  @PathVariable("username") String username,
                                                  @RequestParam(required = false, defaultValue = "") String classroomName,
                                                  @RequestParam(required = false, defaultValue = "classroomId") String sort) {
        Sort sort1 = Sort.by(Sort.Direction.ASC, sort);
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort1);
        Page<ClassroomProjection> list = iClassroomService.PagegetClassroomsByTeacherId(username, "%" + classroomName + "%", pageableWithSort);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/add-student-to-class")
    public ResponseEntity<ApiResponse<String>> addStudentToClass(@RequestBody ClassroomStudent classroomStudent) {
        Student student = iStudentService.findStudentByEmail(classroomStudent.getStudent().getEmail());
        if (student == null) {
            return new ResponseEntity<>(ApiResponse.<String>builder()
                    .message("account does not exist")
                    .build(), HttpStatus.BAD_REQUEST);
        }
        Boolean existsStudentInClassroom = iClassroomService.existsStudentInClassroom(student.getStudentId(), classroomStudent.getClassroom().getClassroomId());
        if (existsStudentInClassroom) {
            return new ResponseEntity<>(ApiResponse.<String>builder()
                    .message("students already exist in class")
                    .build(), HttpStatus.BAD_REQUEST);
        }
        classroomStudent.setStudent(student);
        iClassroomService.addStudentToClass(classroomStudent);
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .message("added successfully")
                .build(), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-student-from-class/{studentId}/{classroomId}")
    public ResponseEntity<ApiResponse<String>> deleteStudentFromClassroom(@PathVariable("studentId") Long studentId,
                                                                          @PathVariable("classroomId") Long classroomId) {
        iClassroomService.deleteStudentFromClassroom(studentId, classroomId);
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .message("deleted successfully")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/page-get-all-class-of-student")
    public ResponseEntity<Page<FindAllClassOfStudentProjection>> findAllClassOfStudent(@PageableDefault(page = 0, size = 5) Pageable pageable,
                                                                                       @RequestParam(required = false, defaultValue = "") String classroomName,
                                                                                       @RequestParam(required = false, defaultValue = "classroomName") String sort) {
        Sort sort1 = Sort.by(Sort.Direction.ASC, sort);
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort1);
        Page<FindAllClassOfStudentProjection> list = iStudentClassroom.PageGetAllClassOfStudent(classroomName, pageableWithSort);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/students/{classroomId}")
    public ResponseEntity<Page<StudentProjection>> getClassroomWithStudents(@PageableDefault(page = 0, size = 5) Pageable pageable,
                                                                            @PathVariable("classroomId") Long classroomId,
                                                                            @RequestParam(required = false, defaultValue = "studentName") String sort) {
        Sort sort1 = Sort.by(Sort.Direction.ASC, sort);
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort1);
        Page<StudentProjection> StudentDto = iStudentClassroom.findStudentByClassroomId(classroomId, pageableWithSort);
        if (StudentDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(StudentDto, HttpStatus.OK);
    }
}

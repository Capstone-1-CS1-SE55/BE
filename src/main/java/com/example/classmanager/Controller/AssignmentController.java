package com.example.classmanager.Controller;

import com.example.classmanager.Entity.response.ApiResponse;
import com.example.classmanager.Model.Assignment;
import com.example.classmanager.Service.Assignment.IAssignmentService;
import com.example.classmanager.Service.Question.IQuestionService;
import com.example.classmanager.Service.StudentAnswer.IStudentAnswerService;
import com.example.classmanager.Service.StudentAssignment.IStudentAssignmentService;
import com.example.classmanager.dto.dto.*;
import com.example.classmanager.dto.projection.AssignmentOfClassProjection;
import com.example.classmanager.dto.projection.*;
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

import java.util.List;

@RestController
@RequestMapping("/assignment")
public class AssignmentController {
    @Autowired
    private IAssignmentService iAssignmentService;

    @Autowired
    private IQuestionService iQuestionService;

    @Autowired
    private IStudentAssignmentService iStudentAssignmentService;

    @Autowired
    private IStudentAnswerService iStudentAnswerService;

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TeacherHomeworkProjection>> findAssignmentsByTeacherId(@PathVariable("teacherId") Long teacherId) {
        List<TeacherHomeworkProjection> list = iAssignmentService.findAssignmentsByTeacherId(teacherId);
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/page-get-all-assignment-of-teacher")
    public ResponseEntity<Page<AssignmentOfTeacherDTO>> findAll(@PageableDefault(page = 0, size = 5) Pageable pageable,
                                                             @RequestParam(required = false, defaultValue = "") String title,
                                                             @RequestParam(required = false, defaultValue = "assignmentId") String sort) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Sort sort1 = Sort.by(Sort.Direction.ASC, sort);
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort1);
        Page<AssignmentOfTeacherDTO> list = iAssignmentService.pageFindAssignmentsByTeacherId(authentication.getName(), "%" + title + "%", pageableWithSort);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Assignment>> findById(@PathVariable("id") Long id) {
        Assignment assignment = iAssignmentService.findById(id);
        if(assignment == null) {
            return new ResponseEntity<>(ApiResponse.<Assignment>builder()
                    .message("No assignment found")
                    .build(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ApiResponse.<Assignment>builder()
                .result(assignment)
                .build(), HttpStatus.OK);
    }

    @GetMapping({"/questions/{assignmentId}/{studentId}", "/questions/{assignmentId}"})
    public ResponseEntity<List<?>> getAllQuestionsInAssignment(@PathVariable("assignmentId") Long assignmentId,
                                                                                     @PathVariable(value = "studentId", required = false) Long studentId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (studentId == null) {
            List<QuestionProjection> list = iQuestionService.getAllQuestionsInAssignment(assignmentId, authentication.getName());
            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            List<StudentAnswerProjection> list = iQuestionService.getAllStudentAnswer(assignmentId, studentId, authentication.getName());
            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createNewAssignment(@RequestBody CreateAssignment createAssignment) {
        if(createAssignment == null) {
            return new ResponseEntity<>(ApiResponse.<String>builder()
                    .message("assignment does not exist")
                    .build(), HttpStatus.BAD_REQUEST);
        }
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        iAssignmentService.createNewAssignment(createAssignment, authentication.getName());
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .message("created successfully")
                .build(), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{assignmentId}")
    public ResponseEntity<ApiResponse<String>> deleteAssignment(@PathVariable("assignmentId") Long assignmentId) {
        Assignment assignment = iAssignmentService.findById(assignmentId);
        if (assignment == null) {
            return new ResponseEntity<>(ApiResponse.<String>builder()
                    .message("assignment does not exist")
                    .build(),HttpStatus.NOT_FOUND);
        }
        iAssignmentService.deleteAssignmentWithRelations(assignmentId);
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .message("deleted successfully")
                .build(),HttpStatus.OK);
    }

    @PostMapping("/update/{assignmentId}")
    public ResponseEntity<ApiResponse<String>> updateAssignment(@RequestBody List<QuestionProjectionDTO> assignmentList,
                                                                @RequestParam(required = false) List<Long> classroomIds,
                                                                @PathVariable("assignmentId") Long assignmentId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        iAssignmentService.updateAssignment(assignmentList, assignmentId, authentication.getName(), classroomIds);
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .message("Updated successfully")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/page-student-assignment/{assignmentId}")
    public ResponseEntity<Page<StudentAssignmentProjection>> pageGetAllStudentAssignment(@PathVariable("assignmentId") Long assignmentId,
                                                                                         @PageableDefault(page = 0, size = 5) Pageable pageable,
                                                                                         @RequestParam(required = false, defaultValue = "studentName") String sort, @RequestParam(required = false, defaultValue = "") String studentName)
    {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Sort sort1 = Sort.by(Sort.Direction.ASC, sort);
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort1);
        Page<StudentAssignmentProjection> list = iStudentAssignmentService.pageGetAllStudentAssignment(assignmentId, "%" + studentName + "%", authentication.getName(), pageableWithSort);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/assignment-of-class/{classroomId}")
    public ResponseEntity<Page<AssignmentOfClassProjection>> pageGetAssignmentOfStudent(@PathVariable("classroomId") Long classroomId,
                                                                                        @RequestParam(required = false, defaultValue = "startDate") String sort,
                                                                                        @PageableDefault(page = 0, size = 5
                                                                                        ) Pageable pageable) {
        Sort sort1 = Sort.by(Sort.Direction.DESC, sort);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort1);
        Page<AssignmentOfClassProjection> page = iAssignmentService.pageGetAssignmentOfClass(classroomId, authentication.getName(), pageableWithSort);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/all-question/{assignmentId}")
    public ResponseEntity<AssignmentQDto> getAllQuestionInAssignment(@PathVariable("assignmentId") Long assignmentId) {
        try {
            var authentaction = SecurityContextHolder.getContext().getAuthentication();
            AssignmentQDto assignmentList = iQuestionService.getAllQuestionInAssignment(assignmentId, authentaction.getName());
            return new ResponseEntity<>(assignmentList, HttpStatus.OK);
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/submit-assignment/{assignmentId}")
    public ResponseEntity<ApiResponse<String>> submitAssignment(@RequestBody List<StudentAnswerDto> list,
                                                                @PathVariable("assignmentId") Long assignmentId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        iAssignmentService.submitAssignment(list, authentication.getName(), assignmentId);
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .message("Submit successfully")
                .build(), HttpStatus.OK);
    }
    @PatchMapping("/update-status")
    public ResponseEntity<ApiResponse<String>> updateStatusAssignmentAndStudentAssignment() {
        iAssignmentService.updateStatusAssignmentAndStudentAssignment();
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .build(), HttpStatus.OK);
    }
    @GetMapping("/assignment-list")
    public ResponseEntity<Page<AssignmentListProjection>> pageAssignmentOfStudent(@PageableDefault(page = 0, size = 5) Pageable pageable,
                                                                                  @RequestParam(required = false, defaultValue = "startDate") String sort) {
        Sort sort1 = Sort.by(Sort.Direction.DESC, sort);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort1);
        Page<AssignmentListProjection> page = iAssignmentService.getAssignmentList(authentication.getName(), pageableWithSort);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PatchMapping("/test-scoring/{assignmentId}/{studentId}")
    public ResponseEntity<ApiResponse<String>> testScoring(@PathVariable("assignmentId") Long assignmentId,
                                                           @PathVariable("studentId") Long studentId,
                                                           @RequestBody List<StudentAnswerScoreDto> list) {
        iStudentAnswerService.testScoring(list, assignmentId, studentId);
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .result("grading successfully")
                .build(), HttpStatus.CREATED);
    }
}

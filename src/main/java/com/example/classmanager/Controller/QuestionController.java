package com.example.classmanager.Controller;

import com.example.classmanager.Entity.response.ApiResponse;
import com.example.classmanager.Model.Question;
import com.example.classmanager.Model.Student;
import com.example.classmanager.Model.StudentAnswer;
import com.example.classmanager.Service.Question.IQuestionService;
import com.example.classmanager.Service.Student.IStudentService;
import com.example.classmanager.dto.dto.QuestionUpdateDto;
import com.example.classmanager.dto.dto.StudentAnswerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private IQuestionService iQuestionService;

    @Autowired
    private IStudentService iStudentService;

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<String>> updateQuestionAndTeacherAnswer(@RequestBody QuestionUpdateDto questionUpdateDto) {
        iQuestionService.updateQuestionAndTeacherAnswer(questionUpdateDto);
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .message("Updated successfully")
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<ApiResponse<String>> deleteQuestionWithRelations(@PathVariable("questionId") Long questionId) {
        iQuestionService.deleteQuestionWithRelations(questionId);
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .message("deleted successfully")
                .build(),HttpStatus.OK);
    }

    @PostMapping("/save-student-answer")
    public ResponseEntity<ApiResponse<String>> saveStudentAnswer(@RequestBody List<StudentAnswerDto> list) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        iQuestionService.saveStudentAnswer(list, authentication.getName());
        return new ResponseEntity<>(ApiResponse.<String>builder()
                .message("Save successfully")
                .build(), HttpStatus.OK);
    }
}

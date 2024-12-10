package com.example.classmanager.Controller;

import com.example.classmanager.Entity.response.ApiResponse;
import com.example.classmanager.Model.Question;
import com.example.classmanager.Service.Question.IQuestionService;
import com.example.classmanager.dto.QuestionUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private IQuestionService iQuestionService;

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
}

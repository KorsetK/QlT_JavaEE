package com.example.controller;

import com.example.entity.Question;
import com.example.entity.User;
import com.example.service.QuestionService;
import com.example.service.WrongBookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private WrongBookService wrongBookService;

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable("id") Integer id) {
        Question question = questionService.getQuestionById(id);
        if (question != null) {
            return ResponseEntity.ok(question);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Map<String, Object>> submitAnswer(
            @PathVariable("id") Integer id,
            @RequestParam("answer") String answer,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "未登录用户");
            return ResponseEntity.status(401).body(response);
        }

        Question question = questionService.getQuestionById(id);
        if (question == null) {
            response.put("success", false);
            response.put("message", "题目不存在");
            return ResponseEntity.status(404).body(response);
        }

        boolean correct = question.getCorrectAnswer().equalsIgnoreCase(answer.trim());
        response.put("correct", correct);
        response.put("correctAnswer", question.getCorrectAnswer());

        if (correct) {
            wrongBookService.removeWrongQuestion(user.getId(), id);
        } else {
            wrongBookService.recordWrongQuestion(user.getId(), id);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addQuestion(@RequestBody Question question, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.put("success", false);
            response.put("message", "无管理员权限");
            return ResponseEntity.status(403).body(response);
        }

        boolean success = questionService.addQuestion(question);
        response.put("success", success);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateQuestion(
            @PathVariable("id") Integer id,
            @RequestBody Question question,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.put("success", false);
            response.put("message", "无管理员权限");
            return ResponseEntity.status(403).body(response);
        }

        question.setId(id);
        boolean success = questionService.updateQuestion(question);
        response.put("success", success);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteQuestion(
            @PathVariable("id") Integer id,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.put("success", false);
            response.put("message", "无管理员权限");
            return ResponseEntity.status(403).body(response);
        }

        boolean success = questionService.deleteQuestion(id);
        response.put("success", success);
        return ResponseEntity.ok(response);
    }
}
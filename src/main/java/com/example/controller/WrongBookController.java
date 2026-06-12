package com.example.controller;

import com.example.entity.Question;
import com.example.entity.User;
import com.example.service.WrongBookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wrong-book")
public class WrongBookController {

    @Autowired
    private WrongBookService wrongBookService;

    @GetMapping
    public ResponseEntity<Object> getMyWrongQuestions(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "未登录用户");
            return ResponseEntity.status(401).body(response);
        }
        List<Question> wrongQuestions = wrongBookService.getUserWrongQuestions(user.getId());
        return ResponseEntity.ok(wrongQuestions);
    }
}
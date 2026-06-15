package com.example.controller;

import com.example.entity.User;
import com.example.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        User user = authService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            response.put("success", true);
            response.put("role", user.getRole());
            response.put("username", user.getUsername());
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        response.put("message", "用户名或密码错误");
        return ResponseEntity.status(401).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        
        Map<String, Object> response = new HashMap<>();
        
        // 验证输入
        if (username == null || username.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "用户名不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (password == null || password.length() < 6) {
            response.put("success", false);
            response.put("message", "密码长度至少为6位");
            return ResponseEntity.badRequest().body(response);
        }
        
        boolean success = authService.register(username.trim(), password);
        if (success) {
            response.put("success", true);
            response.put("message", "注册成功，请登录");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "用户名已存在");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> currentUser(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            response.put("loggedIn", true);
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("userId", user.getId());
            return ResponseEntity.ok(response);
        }
        response.put("loggedIn", false);
        return ResponseEntity.ok(response);
    }
}
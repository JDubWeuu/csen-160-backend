package com.csen160.ecommercebackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "E-Commerce Backend API");
        response.put("version", "1.0.0");
        response.put("endpoints", Map.of(
            "register", "/api/auth/register",
            "login", "/api/auth/login",
            "profile", "/api/users/profile (requires authentication)"
        ));
        return ResponseEntity.ok(response);
    }
}


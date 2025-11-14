package com.csen160.ecommercebackend.controller;

import com.csen160.ecommercebackend.dto.AuthResponse;
import com.csen160.ecommercebackend.dto.LoginRequest;
import com.csen160.ecommercebackend.dto.RegisterRequest;
import com.csen160.ecommercebackend.model.User;
import com.csen160.ecommercebackend.security.JwtTokenProvider;
import com.csen160.ecommercebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User savedUser = userService.registerUser(registerRequest);
            return ResponseEntity.ok("User registered successfully! Username: " + savedUser.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        if (tokenProvider.validateToken(token)) {
            // Extract username from token
            String username = tokenProvider.getUsernameFromToken(token);
            
            // Load user details to get userId and other info
            try {
                User user = (User) userService.loadUserByUsername(username);
                
                // Return user information in response headers
                return ResponseEntity.ok()
                    .header("X-User-Id", user.getId().toString())
                    .header("X-Username", user.getUsername())
                    .header("X-User-Email", user.getEmail())
                    .build();
            } catch (Exception e) {
                return ResponseEntity.status(401).body("User not found");
            }
        } else {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }
}
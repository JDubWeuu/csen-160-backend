package com.csen160.ecommercebackend.controller;

import com.csen160.ecommercebackend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This DTO will safely expose profile data without the password
class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    public UserProfileResponse(Long id, String username, String email, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Add Getters...
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}


@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal User currentUser) {
        // @AuthenticationPrincipal automatically injects the logged-in user details
        if (currentUser == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        // Return a DTO, never the full User object (which has the password)
        UserProfileResponse profile = new UserProfileResponse(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getFirstName(),
                currentUser.getLastName()
        );

        return ResponseEntity.ok(profile);
    }
}
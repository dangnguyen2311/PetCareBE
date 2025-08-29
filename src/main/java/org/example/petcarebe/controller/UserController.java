package org.example.petcarebe.controller;

import lombok.RequiredArgsConstructor;
import org.example.petcarebe.dto.request.CreateUserRequest;
import org.example.petcarebe.dto.response.UserResponse;
import org.example.petcarebe.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/public/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            String password = request.getPassword();
            String username = request.getUsername();
            int maxPasswordLength = Integer.valueOf(System.getenv("MAX_PASSWORD_LENGTH"));
            int minPasswordLength = Integer.parseInt(System.getenv("MIN_PASSWORD_LENGTH"));
            int minUsernameLength = Integer.parseInt(System.getenv("MIN_USERNAME_LENGTH"));
            int maxUsernameLength = Integer.parseInt(System.getenv("MAX_USERNAME_LENGTH"));
            if (password.length() < minPasswordLength) {
                throw new RuntimeException("Password must be at least 3 characters");
            }
            if (username.length() < minUsernameLength || username.length() > maxUsernameLength) {
                throw new RuntimeException("Username must be between 3 and 50 characters");
            }

            UserResponse response = userService.createUser(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.out.println("Error creating user: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(new UserResponse(null, null, null, e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> getListUsers() {
        try{
            List<UserResponse> users = userService.getListUsers();
            return ResponseEntity.ok(users);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(null);
        }
    }
}
package org.example.petcarebe.controller.admin;

import org.example.petcarebe.dto.request.user.UpdateUserStatusRequest;
import org.example.petcarebe.dto.response.user.UserResponse;
import org.example.petcarebe.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user-admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<UserResponse> updateUserStatus(@PathVariable Long userId, @RequestBody UpdateUserStatusRequest request) {
        UserResponse updatedUser = adminService.updateUserStatus(userId, request);
        return ResponseEntity.ok(updatedUser);
    }
}


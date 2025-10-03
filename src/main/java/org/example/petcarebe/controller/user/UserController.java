package org.example.petcarebe.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.petcarebe.dto.request.user.ChangePasswordRequest;
import org.example.petcarebe.dto.request.user.CreateUserRequest;
import org.example.petcarebe.dto.request.user.UpdateUserRequest;
import org.example.petcarebe.dto.request.user.UpdateUserRoleRequest;
import org.example.petcarebe.dto.response.user.UserResponse;
import org.example.petcarebe.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/public/users")
@RequiredArgsConstructor
@Tag(name = "👤 User Management", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Create new user",
            description = "Create a new user account with username, password and role"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or username already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            String password = request.getPassword();
            String username = request.getUsername();
            System.out.println(password + " " + username + request.getRole());
//            int maxPasswordLength = Integer.parseInt(System.getenv("MAX_PASSWORD_LENGTH"));
//            int minPasswordLength = Integer.parseInt(System.getenv("MIN_PASSWORD_LENGTH"));
//            int minUsernameLength = Integer.parseInt(System.getenv("MIN_USERNAME_LENGTH"));
//            int maxUsernameLength = Integer.parseInt(System.getenv("MAX_USERNAME_LENGTH"));
//            if (password.length() < minPasswordLength) {
//                throw new RuntimeException("Password must be at least 3 characters");
//            }
//            if (username.length() < minUsernameLength || username.length() > maxUsernameLength) {
//                throw new RuntimeException("Username must be between 3 and 50 characters");
//            }

            UserResponse response = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            System.out.println("Error creating user: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(new UserResponse(null, null, null,false, null, e.getMessage()));
        }
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> getListUsers() {
        try{
            List<UserResponse> users = userService.getListUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a specific user by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID of the user", example = "1", required = true)
            @PathVariable Long id) {
        try {
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Update user information",
            description = "Update user's username and/or profile image"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or username already exists"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID of the user", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            UserResponse updatedUser = userService.updateUser(id, request);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new UserResponse(null, null, null, false, null, e.getMessage()));
        }
    }

    @Operation(
            summary = "Change user password",
            description = "Change user's password with current password verification"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password data or current password incorrect"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}/change-password")
    public ResponseEntity<UserResponse> changePassword(
            @Parameter(description = "ID of the user", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        try {
            UserResponse updatedUser = userService.changePassword(id, request);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new UserResponse(null, null, null, false, null, e.getMessage()));
        }
    }

    // ==================== ADMIN ENDPOINTS ====================

    @Operation(
            summary = "[ADMIN] Update user role",
            description = "Admin: Update user's role (ADMIN, USER, DOCTOR, STAFF)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid role data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/admin/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole(
            @Parameter(description = "ID of the user", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRoleRequest request) {
        try {
            UserResponse updatedUser = userService.updateUserRole(id, request);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new UserResponse(null, null, null, false, null, e.getMessage()));
        }
    }

    @Operation(
            summary = "[ADMIN] Delete user",
            description = "Admin: Soft delete a user (mark as deleted)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<UserResponse> deleteUser(
            @Parameter(description = "ID of the user", example = "1", required = true)
            @PathVariable Long id) {
        try {
            UserResponse deletedUser = userService.deleteUser(id);
            return ResponseEntity.ok(deletedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "[ADMIN] Restore user",
            description = "Admin: Restore a soft-deleted user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User restored successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/admin/{id}/restore")
    public ResponseEntity<UserResponse> restoreUser(
            @Parameter(description = "ID of the user", example = "1", required = true)
            @PathVariable Long id) {
        try {
            UserResponse restoredUser = userService.restoreUser(id);
            return ResponseEntity.ok(restoredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Get users by role",
            description = "Retrieve all users with a specific role"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> getUsersByRole(
            @Parameter(description = "User role", example = "DOCTOR", required = true)
            @PathVariable String role) {
        try {
            List<UserResponse> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(
            summary = "Search users by username",
            description = "Search users by username (case-insensitive partial match)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(
            @Parameter(description = "Username to search", example = "john", required = true)
            @RequestParam String username) {
        try {
            List<UserResponse> users = userService.searchUsersByUsername(username);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

package org.example.petcarebe.service;

import lombok.RequiredArgsConstructor;
import org.example.petcarebe.dto.request.user.ChangePasswordRequest;
import org.example.petcarebe.dto.request.user.CreateUserRequest;
import org.example.petcarebe.dto.request.user.UpdateUserRequest;
import org.example.petcarebe.dto.request.user.UpdateUserRoleRequest;
import org.example.petcarebe.dto.response.user.UserResponse;
import org.example.petcarebe.model.User;
import org.example.petcarebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.validation.password.min-length}")
    private int minPasswordLength;

    @Value("${app.validation.password.max-length}")
    private int maxPasswordLength;

    @Value("${app.validation.username.min-length}")
    private int minUsernameLength;

    @Value("${app.validation.username.max-length}")
    private int maxUsernameLength;

    public UserResponse createUser(CreateUserRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if(request.getPassword().length()<minPasswordLength) {
            throw new RuntimeException("Password length less than "+minPasswordLength);
        }
        if(request.getPassword().length()>maxPasswordLength) {
            throw new RuntimeException("Password length less than "+maxPasswordLength);
        }
        if(request.getUsername().length()<minUsernameLength) {
            throw new RuntimeException("Username length less than "+minUsernameLength);
        }
        if(request.getUsername().length()>maxUsernameLength) {
            throw new RuntimeException("Username length less than "+maxUsernameLength);
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());
        user.setIsDeleted(false);
        User savedUser = userRepository.save(user);

        return new UserResponse(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getRole(),
            savedUser.getIsDeleted(),
            savedUser.getImgUrl(),
            "User created successfully"
        );
    }

    public List<UserResponse> getListUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole(),
                        user.getIsDeleted(),
                        user.getImgUrl(),
                        null
                )).toList();
    }

    /**
     * Get user by ID
     */
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getIsDeleted(),
                user.getImgUrl(),
                null
        );
    }

    /**
     * Update user information
     */
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Check if new username already exists (if username is being changed)
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }

            // Validate username length
            if (request.getUsername().length() < minUsernameLength) {
                throw new RuntimeException("Username length less than " + minUsernameLength);
            }
            if (request.getUsername().length() > maxUsernameLength) {
                throw new RuntimeException("Username length greater than " + maxUsernameLength);
            }

            user.setUsername(request.getUsername());
        }

        if (request.getImgUrl() != null) {
            user.setImgUrl(request.getImgUrl());
        }

        User updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getRole(),
                updatedUser.getIsDeleted(),
                updatedUser.getImgUrl(),
                "User updated successfully"
        );
    }

    /**
     * Change user password
     */
    @Transactional
    public UserResponse changePassword(Long id, ChangePasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Validate new password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        // Validate new password length
        if (request.getNewPassword().length() < minPasswordLength) {
            throw new RuntimeException("New password length less than " + minPasswordLength);
        }
        if (request.getNewPassword().length() > maxPasswordLength) {
            throw new RuntimeException("New password length greater than " + maxPasswordLength);
        }

        // Check if new password is different from current password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        User updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getRole(),
                updatedUser.getIsDeleted(),
                updatedUser.getImgUrl(),
                "Password changed successfully"
        );
    }

    /**
     * Update user role (Admin only)
     */
    @Transactional
    public UserResponse updateUserRole(Long id, UpdateUserRoleRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        String oldRole = user.getRole();
        user.setRole(request.getRole().toUpperCase());

        User updatedUser = userRepository.save(user);

        String message = String.format("User role updated from %s to %s", oldRole, request.getRole());
        if (request.getReason() != null && !request.getReason().trim().isEmpty()) {
            message += ". Reason: " + request.getReason();
        }

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getRole(),
                updatedUser.getIsDeleted(),
                updatedUser.getImgUrl(),
                message
        );
    }

    /**
     * Soft delete user
     */
    @Transactional
    public UserResponse deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setIsDeleted(true);
        User updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getRole(),
                updatedUser.getIsDeleted(),
                updatedUser.getImgUrl(),
                "User deleted successfully"
        );
    }

    /**
     * Restore deleted user
     */
    @Transactional
    public UserResponse restoreUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setIsDeleted(false);
        User updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getRole(),
                updatedUser.getIsDeleted(),
                updatedUser.getImgUrl(),
                "User restored successfully"
        );
    }

    /**
     * Get users by role
     */
    public List<UserResponse> getUsersByRole(String role) {
        return userRepository.findByRole(role.toUpperCase()).stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole(),
                        user.getIsDeleted(),
                        user.getImgUrl(),
                        null
                )).toList();
    }

    /**
     * Search users by username
     */
    public List<UserResponse> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username).stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole(),
                        user.getIsDeleted(),
                        user.getImgUrl(),
                        null
                )).toList();
    }
}
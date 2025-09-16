package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.user.UpdateUserStatusRequest;
import org.example.petcarebe.dto.response.user.UserResponse;
import org.example.petcarebe.model.User;
import org.example.petcarebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse updateUserStatus(Long userId, UpdateUserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setIsDeleted(request.getIsDeleted());
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getIsDeleted(),
                user.getImgUrl(),
                ""
        );
    }
    private UserResponse convertToResponse(User user, String message) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getIsDeleted(),
                user.getImgUrl(),
                message
        );
    }
}


package org.example.petcarebe.service;

import lombok.RequiredArgsConstructor;
import org.example.petcarebe.dto.request.CreateUserRequest;
import org.example.petcarebe.dto.response.UserResponse;
import org.example.petcarebe.model.User;
import org.example.petcarebe.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());

        User savedUser = userRepository.save(user);

        return new UserResponse(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getRole(),
            "User created successfully"
        );
    }

    public List<UserResponse> getListUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole(),
                        null
                )).toList();
    }
}
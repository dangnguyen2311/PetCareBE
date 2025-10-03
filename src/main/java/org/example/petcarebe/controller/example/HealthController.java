package org.example.petcarebe.controller.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/*
    * DELETE LATER
 */
@RestController
@RequestMapping("/api/public")
@Tag(name = "🏥 Health Check", description = "Public health check endpoints")
public class HealthController {

    @Operation(
            summary = "Application health check",
            description = "Returns the current status and basic information about the application"
    )
    @ApiResponse(responseCode = "200", description = "Application is running")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("application", "Pet Care Backend");
        health.put("version", "1.0.0");
        health.put("message", "Application is running successfully");
        
        return ResponseEntity.ok(health);
    }

    @Operation(
            summary = "Test endpoint",
            description = "Simple test endpoint to verify API is accessible"
    )
    @ApiResponse(responseCode = "200", description = "Test successful")
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test endpoint working!");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }
}

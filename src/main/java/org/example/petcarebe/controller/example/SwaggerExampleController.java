package org.example.petcarebe.controller.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user/v1/swagger-example")
@Tag(name = "📚 Swagger Example", description = "Example endpoints demonstrating Swagger documentation features")
public class SwaggerExampleController {

    @Operation(
            summary = "Get system information",
            description = """
                    Returns basic system information including:
                    - Application name and version
                    - Current server time
                    - Available endpoints count
                    - System status
                    
                    This endpoint is useful for health checks and system monitoring.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "System information retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(
                                    name = "Success Response",
                                    value = """
                                            {
                                              "applicationName": "Pet Care Backend",
                                              "version": "1.0.0",
                                              "serverTime": "2024-01-15T10:30:00",
                                              "status": "RUNNING",
                                              "endpointsCount": 150
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/system-info")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("applicationName", "Pet Care Backend");
        systemInfo.put("version", "1.0.0");
        systemInfo.put("serverTime", java.time.LocalDateTime.now());
        systemInfo.put("status", "RUNNING");
        systemInfo.put("endpointsCount", 150);
        
        return ResponseEntity.ok(systemInfo);
    }

    @Operation(
            summary = "Get user statistics",
            description = "Retrieve statistics for a specific user by ID. (Authentication disabled for testing)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User statistics retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user-stats/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Map<String, Object>> getUserStats(
            @Parameter(
                    description = "ID of the user to get statistics for",
                    example = "123",
                    required = true
            )
            @PathVariable Long userId) {
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("userId", userId);
        stats.put("totalPets", 3);
        stats.put("totalAppointments", 15);
        stats.put("totalVisits", 12);
        stats.put("lastVisitDate", "2024-01-10");
        
        return ResponseEntity.ok(stats);
    }

    @Operation(
            summary = "Search with filters",
            description = "Advanced search endpoint with multiple optional parameters"
    )
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @Parameter(description = "Search keyword", example = "Golden Retriever")
            @RequestParam(required = false) String keyword,
            
            @Parameter(description = "Filter by category", example = "pets")
            @RequestParam(required = false) String category,
            
            @Parameter(description = "Page number for pagination", example = "1")
            @RequestParam(defaultValue = "1") int page,
            
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> result = new HashMap<>();
        result.put("keyword", keyword);
        result.put("category", category);
        result.put("page", page);
        result.put("size", size);
        result.put("totalResults", 25);
        result.put("results", "Search results would be here...");
        
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Create example data",
            description = "Example POST endpoint with request body validation"
    )
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createExample(
            @Parameter(description = "Example data to create")
            @RequestBody Map<String, Object> data) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", 123);
        response.put("message", "Example data created successfully");
        response.put("createdAt", java.time.LocalDateTime.now());
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }
}

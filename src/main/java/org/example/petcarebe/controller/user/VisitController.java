package org.example.petcarebe.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.visit.UpdateVisitRequest;
import org.example.petcarebe.dto.response.visit.VisitResponse;
import org.example.petcarebe.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/visits")
@Tag(name = "🏥 Visit Management (User)", description = "Admin endpoints for managing visits and medical records")
public class VisitController {
    @Autowired
    private VisitService visitService;
    @GetMapping("/{id}")
    public ResponseEntity<VisitResponse> getVisitById(
            @Parameter(description = "Visit ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            VisitResponse response = visitService.getVisitById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error getting visit: " + e.getMessage());

            VisitResponse errorResponse = new VisitResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());

            VisitResponse errorResponse = new VisitResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get visits by pet ID
     * Accessible by all authenticated users
     */
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<VisitResponse>> getVisitsByPetId(@PathVariable Long petId) {
        try {
            List<VisitResponse> visits = visitService.getVisitsByPetId(petId);
            return ResponseEntity.ok(visits);
        } catch (Exception e) {
            System.err.println("Error getting visits by pet: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

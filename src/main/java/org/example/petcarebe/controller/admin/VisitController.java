package org.example.petcarebe.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.visit.CreateVisitRequest;
import org.example.petcarebe.dto.request.visit.UpdateVisitRequest;
import org.example.petcarebe.dto.request.visit.VisitDateRangeRequest;
import org.example.petcarebe.dto.response.visit.CreateVisitResponse;
import org.example.petcarebe.dto.response.visit.VisitResponse;
import org.example.petcarebe.dto.response.visit.VisitMedicalSummaryResponse;
import org.example.petcarebe.dto.response.visit.VisitStatisticsResponse;
import org.example.petcarebe.dto.response.diagnosis.DiagnosisResponse;
import org.example.petcarebe.dto.response.testresult.TestResultResponse;
import org.example.petcarebe.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/visits")
@Tag(name = "🏥 Visit Management (Admin)", description = "Admin endpoints for managing visits and medical records")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @Operation(
            summary = "Create a new visit",
            description = "Create a new visit record for a pet. Only ADMIN and DOCTOR roles can create visits."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Visit created successfully",
                    content = @Content(schema = @Schema(implementation = CreateVisitResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/create-visit")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<CreateVisitResponse> createVisit(
            @Parameter(description = "Visit creation request", required = true)
            @Valid @RequestBody CreateVisitRequest request) {
        try{
            CreateVisitResponse response = visitService.createVisit(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (RuntimeException e) {
            CreateVisitResponse error = new CreateVisitResponse();
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        catch(Exception e){
            CreateVisitResponse error = new CreateVisitResponse();
            error.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(
            summary = "Get all visits",
            description = "Retrieve a list of all visits in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visits retrieved successfully",
                    content = @Content(schema = @Schema(implementation = VisitResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<VisitResponse>> getAllVisits() {
        try {
            List<VisitResponse> visits = visitService.getAllVisits();
            return ResponseEntity.ok(visits);
        } catch (Exception e) {
            System.err.println("Error getting all visits: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Get visit by ID",
            description = "Retrieve detailed information about a specific visit"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visit found and retrieved successfully",
                    content = @Content(schema = @Schema(implementation = VisitResponse.class))),
            @ApiResponse(responseCode = "404", description = "Visit not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
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

    @Operation(
            summary = "Update visit",
            description = "Update an existing visit record. Only ADMIN and DOCTOR roles can update visits."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visit updated successfully",
                    content = @Content(schema = @Schema(implementation = VisitResponse.class))),
            @ApiResponse(responseCode = "404", description = "Visit not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<VisitResponse> updateVisit(
            @Parameter(description = "Visit ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Visit update request", required = true)
            @Valid @RequestBody UpdateVisitRequest request) {
        try {
            VisitResponse response = visitService.updateVisit(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error updating visit: " + e.getMessage());

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
     * Delete visit
     * Only ADMIN can delete visits
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteVisit(@PathVariable Long id) {
        try {
            visitService.deleteVisit(id);
            return ResponseEntity.ok("Visit deleted successfully");
        } catch (RuntimeException e) {
            System.err.println("Error deleting visit: " + e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
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

    /**
     * Get visits by customer ID
     * Accessible by all authenticated users
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<VisitResponse>> getVisitsByCustomerId(@PathVariable Long customerId) {
        try {
            List<VisitResponse> visits = visitService.getVisitsByCustomerId(customerId);
            return ResponseEntity.ok(visits);
        } catch (Exception e) {
            System.err.println("Error getting visits by customer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get visits by doctor ID
     * Accessible by all authenticated users
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<VisitResponse>> getVisitsByDoctorId(@PathVariable Long doctorId) {
        try {
            List<VisitResponse> visits = visitService.getVisitsByDoctorId(doctorId);
            return ResponseEntity.ok(visits);
        } catch (Exception e) {
            System.err.println("Error getting visits by doctor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get visits by date
     * Accessible by all authenticated users
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<VisitResponse>> getVisitsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<VisitResponse> visits = visitService.getVisitsByDate(date);
            return ResponseEntity.ok(visits);
        } catch (Exception e) {
            System.err.println("Error getting visits by date: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get visits by date range
     * Accessible by all authenticated users
     */
    @PostMapping("/date-range")
    public ResponseEntity<List<VisitResponse>> getVisitsByDateRange(@Valid @RequestBody VisitDateRangeRequest request) {
        try {
            List<VisitResponse> visits = visitService.getVisitsByDateRange(request);
            return ResponseEntity.ok(visits);
        } catch (Exception e) {
            System.err.println("Error getting visits by date range: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get diagnoses for a visit
     * Accessible by all authenticated users
     */
    @GetMapping("/{id}/diagnoses")
    public ResponseEntity<List<DiagnosisResponse>> getVisitDiagnoses(@PathVariable Long id) {
        try {
            List<DiagnosisResponse> diagnoses = visitService.getVisitDiagnoses(id);
            return ResponseEntity.ok(diagnoses);
        } catch (RuntimeException e) {
            System.err.println("Error getting visit diagnoses: " + e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get test results for a visit
     * Accessible by all authenticated users
     */
    @GetMapping("/{id}/test-results")
    public ResponseEntity<List<TestResultResponse>> getVisitTestResults(@PathVariable Long id) {
        try {
            List<TestResultResponse> testResults = visitService.getVisitTestResults(id);
            return ResponseEntity.ok(testResults);
        } catch (RuntimeException e) {
            System.err.println("Error getting visit test results: " + e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Get medical summary for a visit",
            description = "Retrieve comprehensive medical summary including diagnoses and test results for a specific visit"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical summary retrieved successfully",
                    content = @Content(schema = @Schema(implementation = VisitMedicalSummaryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Visit not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}/medical-summary")
    public ResponseEntity<VisitMedicalSummaryResponse> getVisitMedicalSummary(
            @Parameter(description = "Visit ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            VisitMedicalSummaryResponse summary = visitService.getVisitMedicalSummary(id);
            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            System.err.println("Error getting visit medical summary: " + e.getMessage());

            VisitMedicalSummaryResponse errorResponse = new VisitMedicalSummaryResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());

            VisitMedicalSummaryResponse errorResponse = new VisitMedicalSummaryResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
            summary = "Get daily visit statistics",
            description = "Retrieve daily visit statistics for a specified date range. Only ADMIN role can access this endpoint."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = VisitStatisticsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date range",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/statistics/daily")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VisitStatisticsResponse> getDailyStatistics(
            @Parameter(description = "Date range for statistics", required = true)
            @Valid @RequestBody VisitDateRangeRequest request) {
        try {
            VisitStatisticsResponse statistics = visitService.getDailyStatistics(request);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            System.err.println("Error getting daily statistics: " + e.getMessage());

            VisitStatisticsResponse errorResponse = new VisitStatisticsResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get monthly visit statistics
     * Only ADMIN can access statistics
     */
    @PostMapping("/statistics/monthly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VisitStatisticsResponse> getMonthlyStatistics(@Valid @RequestBody VisitDateRangeRequest request) {
        try {
            VisitStatisticsResponse statistics = visitService.getMonthlyStatistics(request);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            System.err.println("Error getting monthly statistics: " + e.getMessage());

            VisitStatisticsResponse errorResponse = new VisitStatisticsResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get doctor performance statistics
     * Only ADMIN can access statistics
     */
    @PostMapping("/statistics/doctor-performance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VisitStatisticsResponse> getDoctorPerformanceStatistics(@Valid @RequestBody VisitDateRangeRequest request) {
        try {
            VisitStatisticsResponse statistics = visitService.getDoctorPerformanceStatistics(request);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            System.err.println("Error getting doctor performance statistics: " + e.getMessage());

            VisitStatisticsResponse errorResponse = new VisitStatisticsResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}


package org.example.petcarebe.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.vaccineschedule.CreateVaccineScheduleRequest;
import org.example.petcarebe.dto.request.vaccineschedule.UpdateVaccineScheduleRequest;
import org.example.petcarebe.dto.request.vaccineschedule.RescheduleVaccineRequest;
import org.example.petcarebe.dto.request.vaccineschedule.VaccineScheduleDateRangeRequest;
import org.example.petcarebe.dto.response.vaccineschedule.VaccineScheduleResponse;
import org.example.petcarebe.dto.response.vaccineschedule.VaccineScheduleStatisticsResponse;
import org.example.petcarebe.service.VaccineScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/vaccine-schedules")
@Tag(name = "💉 Vaccine Schedule Management (Admin)", description = "Admin endpoints for managing vaccine schedules")
public class VaccineScheduleController {

    @Autowired
    private VaccineScheduleService vaccineScheduleService;

    @Operation(
            summary = "Create a new vaccine schedule",
            description = "Create a new vaccine schedule for a pet. Only ADMIN and DOCTOR roles can create schedules."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vaccine schedule created successfully",
                    content = @Content(schema = @Schema(implementation = VaccineScheduleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<VaccineScheduleResponse> createVaccineSchedule(
            @Parameter(description = "Vaccine schedule creation request", required = true)
            @Valid @RequestBody CreateVaccineScheduleRequest request) {
        try {
            VaccineScheduleResponse response = vaccineScheduleService.createVaccineSchedule(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            System.err.println("Error creating vaccine schedule: " + e.getMessage());

            VaccineScheduleResponse errorResponse = new VaccineScheduleResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());

            VaccineScheduleResponse errorResponse = new VaccineScheduleResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
            summary = "Get all vaccine schedules",
            description = "Retrieve a list of all vaccine schedules in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaccine schedules retrieved successfully",
                    content = @Content(schema = @Schema(implementation = VaccineScheduleResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<VaccineScheduleResponse>> getAllVaccineSchedules() {
        try {
            List<VaccineScheduleResponse> schedules = vaccineScheduleService.getAllVaccineSchedules();
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            System.err.println("Error getting all vaccine schedules: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Get vaccine schedule by ID",
            description = "Retrieve detailed information about a specific vaccine schedule"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaccine schedule found and retrieved successfully",
                    content = @Content(schema = @Schema(implementation = VaccineScheduleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Vaccine schedule not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<VaccineScheduleResponse> getVaccineScheduleById(
            @Parameter(description = "Vaccine schedule ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            VaccineScheduleResponse response = vaccineScheduleService.getVaccineScheduleById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error getting vaccine schedule: " + e.getMessage());

            VaccineScheduleResponse errorResponse = new VaccineScheduleResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());

            VaccineScheduleResponse errorResponse = new VaccineScheduleResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
            summary = "Update vaccine schedule",
            description = "Update an existing vaccine schedule. Only ADMIN and DOCTOR roles can update schedules."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaccine schedule updated successfully",
                    content = @Content(schema = @Schema(implementation = VaccineScheduleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Vaccine schedule not found",
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
    public ResponseEntity<VaccineScheduleResponse> updateVaccineSchedule(
            @Parameter(description = "Vaccine schedule ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Vaccine schedule update request", required = true)
            @Valid @RequestBody UpdateVaccineScheduleRequest request) {
        try {
            VaccineScheduleResponse response = vaccineScheduleService.updateVaccineSchedule(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error updating vaccine schedule: " + e.getMessage());

            VaccineScheduleResponse errorResponse = new VaccineScheduleResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());

            VaccineScheduleResponse errorResponse = new VaccineScheduleResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
            summary = "Delete vaccine schedule",
            description = "Delete a vaccine schedule. Only ADMIN role can delete schedules."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaccine schedule deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Vaccine schedule not found"),
            @ApiResponse(responseCode = "400", description = "Cannot delete schedule with current status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteVaccineSchedule(
            @Parameter(description = "Vaccine schedule ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            vaccineScheduleService.deleteVaccineSchedule(id);
            return ResponseEntity.ok("Vaccine schedule deleted successfully");
        } catch (RuntimeException e) {
            System.err.println("Error deleting vaccine schedule: " + e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @Operation(
            summary = "Get vaccine schedules by pet ID",
            description = "Retrieve all vaccine schedules for a specific pet"
    )
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<VaccineScheduleResponse>> getVaccineSchedulesByPetId(
            @Parameter(description = "Pet ID", required = true, example = "1")
            @PathVariable Long petId) {
        try {
            List<VaccineScheduleResponse> schedules = vaccineScheduleService.getVaccineSchedulesByPetId(petId);
            return ResponseEntity.ok(schedules);
        } catch (RuntimeException e) {
            System.err.println("Error getting vaccine schedules by pet: " + e.getMessage());

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
            summary = "Get vaccine schedules by vaccine ID",
            description = "Retrieve all schedules for a specific vaccine"
    )
    @GetMapping("/vaccine/{vaccineId}")
    public ResponseEntity<List<VaccineScheduleResponse>> getVaccineSchedulesByVaccineId(
            @Parameter(description = "Vaccine ID", required = true, example = "1")
            @PathVariable Long vaccineId) {
        try {
            List<VaccineScheduleResponse> schedules = vaccineScheduleService.getVaccineSchedulesByVaccineId(vaccineId);
            return ResponseEntity.ok(schedules);
        } catch (RuntimeException e) {
            System.err.println("Error getting vaccine schedules by vaccine: " + e.getMessage());

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
            summary = "Get vaccine schedules by status",
            description = "Retrieve all vaccine schedules with a specific status"
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<List<VaccineScheduleResponse>> getVaccineSchedulesByStatus(
            @Parameter(description = "Schedule status", required = true, example = "SCHEDULED")
            @PathVariable String status) {
        try {
            List<VaccineScheduleResponse> schedules = vaccineScheduleService.getVaccineSchedulesByStatus(status);
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            System.err.println("Error getting vaccine schedules by status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Get vaccine schedules by date",
            description = "Retrieve all vaccine schedules for a specific date"
    )
    @GetMapping("/date/{date}")
    public ResponseEntity<List<VaccineScheduleResponse>> getVaccineSchedulesByDate(
            @Parameter(description = "Scheduled date", required = true, example = "2024-01-15")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<VaccineScheduleResponse> schedules = vaccineScheduleService.getVaccineSchedulesByDate(date);
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            System.err.println("Error getting vaccine schedules by date: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Get vaccine schedules by date range",
            description = "Retrieve all vaccine schedules within a specific date range"
    )
    @PostMapping("/date-range")
    public ResponseEntity<List<VaccineScheduleResponse>> getVaccineSchedulesByDateRange(
            @Parameter(description = "Date range request", required = true)
            @Valid @RequestBody VaccineScheduleDateRangeRequest request) {
        try {
            List<VaccineScheduleResponse> schedules = vaccineScheduleService.getVaccineSchedulesByDateRange(request);
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            System.err.println("Error getting vaccine schedules by date range: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Complete vaccine schedule",
            description = "Mark a vaccine schedule as completed. Only ADMIN and DOCTOR roles can complete schedules."
    )
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<VaccineScheduleResponse> completeVaccineSchedule(
            @Parameter(description = "Vaccine schedule ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            VaccineScheduleResponse response = vaccineScheduleService.completeVaccineSchedule(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error completing vaccine schedule: " + e.getMessage());

            VaccineScheduleResponse errorResponse = new VaccineScheduleResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());

            VaccineScheduleResponse errorResponse = new VaccineScheduleResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
            summary = "Reschedule vaccine",
            description = "Reschedule a vaccine appointment to a new date. Only ADMIN and DOCTOR roles can reschedule."
    )
    @PutMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<VaccineScheduleResponse> rescheduleVaccine(
            @Parameter(description = "Vaccine schedule ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Reschedule request", required = true)
            @Valid @RequestBody RescheduleVaccineRequest request) {
        try {
            VaccineScheduleResponse response = vaccineScheduleService.rescheduleVaccine(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error rescheduling vaccine: " + e.getMessage());

            VaccineScheduleResponse errorResponse = new VaccineScheduleResponse();
            errorResponse.setMessage(e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());

            VaccineScheduleResponse errorResponse = new VaccineScheduleResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
            summary = "Get overdue vaccine schedules",
            description = "Retrieve all vaccine schedules that are overdue (past scheduled date and still scheduled)"
    )
    @GetMapping("/overdue")
    public ResponseEntity<List<VaccineScheduleResponse>> getOverdueSchedules() {
        try {
            List<VaccineScheduleResponse> schedules = vaccineScheduleService.getOverdueSchedules();
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            System.err.println("Error getting overdue schedules: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Get upcoming vaccine schedules",
            description = "Retrieve all vaccine schedules that are upcoming (within next 7 days)"
    )
    @GetMapping("/upcoming")
    public ResponseEntity<List<VaccineScheduleResponse>> getUpcomingSchedules() {
        try {
            List<VaccineScheduleResponse> schedules = vaccineScheduleService.getUpcomingSchedules();
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            System.err.println("Error getting upcoming schedules: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Get vaccine schedule statistics",
            description = "Retrieve comprehensive statistics about vaccine schedules. Only ADMIN role can access statistics."
    )
    @PostMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccineScheduleStatisticsResponse> getVaccineScheduleStatistics(
            @Parameter(description = "Date range for statistics", required = true)
            @Valid @RequestBody VaccineScheduleDateRangeRequest request) {
        try {
            VaccineScheduleStatisticsResponse statistics = vaccineScheduleService.getVaccineScheduleStatistics(request);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            System.err.println("Error getting vaccine schedule statistics: " + e.getMessage());

            VaccineScheduleStatisticsResponse errorResponse = new VaccineScheduleStatisticsResponse();
            errorResponse.setMessage("An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

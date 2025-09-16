package org.example.petcarebe.controller.admin;

import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.workschedule.CreateWorkScheduleRequest;
import org.example.petcarebe.dto.request.workschedule.UpdateWorkScheduleRequest;
import org.example.petcarebe.dto.response.workschedule.WorkScheduleResponse;
import org.example.petcarebe.dto.response.workschedule.WorkScheduleStatisticsResponse;
import org.example.petcarebe.service.WorkScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/work-schedules")
public class WorkScheduleController {

    @Autowired
    private WorkScheduleService workScheduleService;

    /**
     * Create a new work schedule
     * Only ADMIN can create work schedules
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkScheduleResponse> createWorkSchedule(@Valid @RequestBody CreateWorkScheduleRequest request) {
        try {
            WorkScheduleResponse response = workScheduleService.createWorkSchedule(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            WorkScheduleResponse errorResponse = new WorkScheduleResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all work schedules
     * Accessible by ADMIN, DOCTOR, STAFF
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'STAFF')")
    public ResponseEntity<List<WorkScheduleResponse>> getAllWorkSchedules() {
        List<WorkScheduleResponse> schedules = workScheduleService.getAllWorkSchedules();
        return ResponseEntity.ok(schedules);
    }

    /**
     * Get work schedule by ID
     * Accessible by all authenticated users
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkScheduleResponse> getWorkScheduleById(@PathVariable Long id) {
        try {
            WorkScheduleResponse schedule = workScheduleService.getWorkScheduleById(id);
            return ResponseEntity.ok(schedule);
        } catch (RuntimeException e) {
            WorkScheduleResponse errorResponse = new WorkScheduleResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get work schedules by doctor ID
     * Accessible by all authenticated users
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<WorkScheduleResponse>> getWorkSchedulesByDoctorId(@PathVariable Long doctorId) {
        try {
            List<WorkScheduleResponse> schedules = workScheduleService.getWorkSchedulesByDoctorId(doctorId);
            return ResponseEntity.ok(schedules);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
    }



    /**
     * Get work schedules by specific work date
     * Accessible by all authenticated users
     */
    @GetMapping("/date/{workDate}")
    public ResponseEntity<List<WorkScheduleResponse>> getWorkSchedulesByDate(@PathVariable LocalDate workDate) {
        List<WorkScheduleResponse> schedules = workScheduleService.getWorkSchedulesByDate(workDate);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Get work schedules by date range
     * Accessible by all authenticated users
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<WorkScheduleResponse>> getWorkSchedulesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<WorkScheduleResponse> schedules = workScheduleService.getWorkSchedulesByDateRange(startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Get work schedules by doctor and date range
     * Accessible by all authenticated users
     */
    @GetMapping("/doctor/{doctorId}/date-range")
    public ResponseEntity<List<WorkScheduleResponse>> getWorkSchedulesByDoctorAndDateRange(
            @PathVariable Long doctorId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            List<WorkScheduleResponse> schedules = workScheduleService.getWorkSchedulesByDoctorAndDateRange(doctorId, startDate, endDate);
            return ResponseEntity.ok(schedules);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
    }

    /**
     * Update work schedule
     * ADMIN can update any schedule, DOCTOR can update their own schedule
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR') and @workScheduleService.isScheduleOwner(#id, authentication.name))")
    public ResponseEntity<WorkScheduleResponse> updateWorkSchedule(@PathVariable Long id,
                                                                  @Valid @RequestBody UpdateWorkScheduleRequest request) {
        try {
            WorkScheduleResponse response = workScheduleService.updateWorkSchedule(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            WorkScheduleResponse errorResponse = new WorkScheduleResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Delete work schedule
     * Only ADMIN can delete work schedules
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkScheduleResponse> deleteWorkSchedule(@PathVariable Long id) {
        try {
            WorkScheduleResponse response = workScheduleService.deleteWorkSchedule(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            WorkScheduleResponse errorResponse = new WorkScheduleResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get work schedule statistics for a doctor
     * ADMIN can view any doctor's stats, DOCTOR can view their own stats
     */
    @GetMapping("/doctor/{doctorId}/statistics")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR') and @doctorService.isDoctorOwner(#doctorId, authentication.name))")
    public ResponseEntity<WorkScheduleStatisticsResponse> getWorkScheduleStatistics(@PathVariable Long doctorId) {
        try {
            WorkScheduleStatisticsResponse stats = workScheduleService.getWorkScheduleStatistics(doctorId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}


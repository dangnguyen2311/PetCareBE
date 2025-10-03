package org.example.petcarebe.controller.admin;

import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.doctor.CreateDoctorRequest;
import org.example.petcarebe.dto.request.doctor.UpdateDoctorRequest;
import org.example.petcarebe.dto.response.doctor.DoctorResponse;
import org.example.petcarebe.dto.response.doctor.DoctorStatisticsResponse;
import org.example.petcarebe.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * Create a new doctor
     * Only ADMIN can create doctors
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody CreateDoctorRequest request) {
        try {
            DoctorResponse response = doctorService.createDoctor(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            DoctorResponse errorResponse = new DoctorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all doctors
     * Accessible by ADMIN, DOCTOR, STAFF
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'STAFF')")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        List<DoctorResponse> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    /**
     * Get active doctors only
     * Accessible by all authenticated users
     */
    @GetMapping("/active")
    public ResponseEntity<List<DoctorResponse>> getActiveDoctors() {
        List<DoctorResponse> doctors = doctorService.getActiveDoctors();
        return ResponseEntity.ok(doctors);
    }

    /**
     * Get doctor by ID
     * Accessible by all authenticated users
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        try {
            DoctorResponse doctor = doctorService.getDoctorById(id);
            return ResponseEntity.ok(doctor);
        } catch (RuntimeException e) {
            DoctorResponse errorResponse = new DoctorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Update doctor information
     * ADMIN can update any doctor, DOCTOR can update their own profile
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR') and @doctorService.isDoctorOwner(#id, authentication.name))")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateDoctorRequest request) {
        try {
            DoctorResponse response = doctorService.updateDoctor(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            DoctorResponse errorResponse = new DoctorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Delete doctor (soft delete)
     * Only ADMIN can delete doctors
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponse> deleteDoctor(@PathVariable Long id) {
        try {
            DoctorResponse response = doctorService.deleteDoctor(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            DoctorResponse errorResponse = new DoctorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get doctors by specialization
     * Accessible by all authenticated users
     */
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<DoctorResponse> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Get current doctor's profile (for authenticated doctor)
     * Only DOCTOR role can access this
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponse> getMyProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            DoctorResponse doctor = doctorService.getDoctorByUsername(username);
            return ResponseEntity.ok(doctor);
        } catch (RuntimeException e) {
            DoctorResponse errorResponse = new DoctorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Update current doctor's profile (for authenticated doctor)
     * Only DOCTOR role can access this
     */
    @PutMapping("/me")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponse> updateMyProfile(@Valid @RequestBody UpdateDoctorRequest request,
                                                         Authentication authentication) {
        try {
            String username = authentication.getName();
            DoctorResponse currentDoctor = doctorService.getDoctorByUsername(username);
            DoctorResponse response = doctorService.updateDoctor(currentDoctor.getId(), request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            DoctorResponse errorResponse = new DoctorResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get doctor statistics
     * ADMIN can view any doctor's stats, DOCTOR can view their own stats
     */
    @GetMapping("/{id}/statistics")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR') and @doctorService.isDoctorOwner(#id, authentication.name))")
    public ResponseEntity<DoctorStatisticsResponse> getDoctorStatistics(@PathVariable Long id) {
        // TODO: Implement statistics calculation
        DoctorStatisticsResponse stats = DoctorStatisticsResponse.builder()
                .doctorId(id)
                .doctorName("Sample Doctor")
                .specialization("General Practice")
                .totalVisits(0)
                .visitsThisMonth(0)
                .visitsThisWeek(0)
                .visitsToday(0)
                .totalPatientsServed(0)
                .uniquePatientsThisMonth(0)
                .totalDiagnoses(0)
                .diagnosesThisMonth(0)
                .totalVaccinations(0)
                .vaccinationsThisMonth(0)
                .scheduledDaysThisMonth(0)
                .workingDaysThisMonth(0)
                .averageVisitsPerDay(0.0)
                .averagePatientsPerDay(0.0)
                .workingSince("N/A")
                .lastWorkDate("N/A")
                .nextScheduledDate("N/A")
                .build();

        return ResponseEntity.ok(stats);
    }
}


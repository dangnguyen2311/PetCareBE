package org.example.petcarebe.controller;

import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.staff.CreateStaffRequest;
import org.example.petcarebe.dto.request.staff.UpdateStaffRequest;
import org.example.petcarebe.dto.response.staff.StaffResponse;
import org.example.petcarebe.dto.response.staff.StaffStatisticsResponse;
import org.example.petcarebe.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    /**
     * Create a new staff member
     * Only ADMIN can create staff
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffResponse> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        try {
            StaffResponse response = staffService.createStaff(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            StaffResponse errorResponse = new StaffResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all staff members
     * Accessible by ADMIN and STAFF
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<StaffResponse>> getAllStaff() {
        List<StaffResponse> staff = staffService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    /**
     * Get all active staff members
     * Accessible by all authenticated users
     */
    @GetMapping("/active")
    public ResponseEntity<List<StaffResponse>> getAllActiveStaff() {
        List<StaffResponse> staff = staffService.getAllActiveStaff();
        return ResponseEntity.ok(staff);
    }

    /**
     * Get staff by ID
     * Accessible by all authenticated users
     */
    @GetMapping("/{id}")
    public ResponseEntity<StaffResponse> getStaffById(@PathVariable Long id) {
        try {
            StaffResponse staff = staffService.getStaffById(id);
            return ResponseEntity.ok(staff);
        } catch (RuntimeException e) {
            StaffResponse errorResponse = new StaffResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get staff by position
     * Accessible by all authenticated users
     */
    @GetMapping("/position/{position}")
    public ResponseEntity<List<StaffResponse>> getStaffByPosition(@PathVariable String position) {
        List<StaffResponse> staff = staffService.getStaffByPosition(position);
        return ResponseEntity.ok(staff);
    }

    /**
     * Search staff by keyword
     * Accessible by all authenticated users
     */
    @GetMapping("/search")
    public ResponseEntity<List<StaffResponse>> searchStaff(@RequestParam String keyword) {
        List<StaffResponse> staff = staffService.searchStaff(keyword);
        return ResponseEntity.ok(staff);
    }

    /**
     * Update staff information
     * ADMIN can update any staff, STAFF can update their own profile
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STAFF') and @staffService.isStaffOwner(#id, authentication.name))")
    public ResponseEntity<StaffResponse> updateStaff(@PathVariable Long id,
                                                    @Valid @RequestBody UpdateStaffRequest request) {
        try {
            StaffResponse response = staffService.updateStaff(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            StaffResponse errorResponse = new StaffResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Delete staff (deactivate account)
     * Only ADMIN can delete staff
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffResponse> deleteStaff(@PathVariable Long id) {
        try {
            StaffResponse response = staffService.deleteStaff(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            StaffResponse errorResponse = new StaffResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get current staff profile
     * STAFF can view their own profile
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<StaffResponse> getMyProfile(Authentication authentication) {
        try {
            StaffResponse response = staffService.getStaffProfile(authentication.getName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            StaffResponse errorResponse = new StaffResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Update current staff profile
     * STAFF can update their own profile
     */
    @PutMapping("/me")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<StaffResponse> updateMyProfile(@Valid @RequestBody UpdateStaffRequest request,
                                                        Authentication authentication) {
        try {
            StaffResponse currentProfile = staffService.getStaffProfile(authentication.getName());
            StaffResponse response = staffService.updateStaff(currentProfile.getId(), request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            StaffResponse errorResponse = new StaffResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get staff statistics
     * ADMIN can view any staff's stats, STAFF can view their own stats
     */
    @GetMapping("/{id}/statistics")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STAFF') and @staffService.isStaffOwner(#id, authentication.name))")
    public ResponseEntity<StaffStatisticsResponse> getStaffStatistics(@PathVariable Long id) {
        try {
            StaffStatisticsResponse stats = staffService.getStaffStatistics(id);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}


package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.staff.CreateStaffRequest;
import org.example.petcarebe.dto.request.staff.UpdateStaffRequest;
import org.example.petcarebe.dto.response.staff.StaffResponse;
import org.example.petcarebe.dto.response.staff.StaffStatisticsResponse;
import org.example.petcarebe.model.Staff;
import org.example.petcarebe.model.User;
import org.example.petcarebe.repository.StaffRepository;
import org.example.petcarebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Create a new staff member
     */
    @Transactional
    public StaffResponse createStaff(CreateStaffRequest request) {
        // Check if email already exists
        if (staffRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }

        // Create User account
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("STAFF");
        user.setIsDeleted(false);

        User savedUser = userRepository.save(user);

        // Create Staff
        Staff staff = new Staff();
        staff.setFullname(request.getFullname());
        staff.setPosition(request.getPosition());
        staff.setPhone(request.getPhone());
        staff.setEmail(request.getEmail());
        staff.setAddress(request.getAddress());
        staff.setUser(savedUser);

        Staff savedStaff = staffRepository.save(staff);
        return convertToResponse(savedStaff, "Staff created successfully");
    }

    /**
     * Get all staff members
     */
    public List<StaffResponse> getAllStaff() {
        return staffRepository.findAllByOrderByFullnameAsc().stream()
                .map(staff -> convertToResponse(staff, null))
                .collect(Collectors.toList());
    }

    /**
     * Get all active staff members
     */
    public List<StaffResponse> getAllActiveStaff() {
        return staffRepository.findByUser_IsDeletedFalse().stream()
                .map(staff -> convertToResponse(staff, null))
                .collect(Collectors.toList());
    }

    /**
     * Get staff by ID
     */
    public StaffResponse getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));
        return convertToResponse(staff, null);
    }

    /**
     * Get staff by position
     */
    public List<StaffResponse> getStaffByPosition(String position) {
        return staffRepository.findByPositionContainingIgnoreCase(position).stream()
                .map(staff -> convertToResponse(staff, null))
                .collect(Collectors.toList());
    }

    /**
     * Search staff by keyword
     */
    public List<StaffResponse> searchStaff(String keyword) {
        return staffRepository.searchByKeyword(keyword).stream()
                .map(staff -> convertToResponse(staff, null))
                .collect(Collectors.toList());
    }

    /**
     * Update staff information
     */
    @Transactional
    public StaffResponse updateStaff(Long id, UpdateStaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));

        // Check email uniqueness if email is being updated
        if (request.getEmail() != null && !request.getEmail().equals(staff.getEmail())) {
            if (staffRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists: " + request.getEmail());
            }
        }

        // Update fields if provided
        if (request.getFullname() != null) {
            staff.setFullname(request.getFullname());
        }
        if (request.getPosition() != null) {
            staff.setPosition(request.getPosition());
        }
        if (request.getPhone() != null) {
            staff.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            staff.setEmail(request.getEmail());
        }
        if (request.getAddress() != null) {
            staff.setAddress(request.getAddress());
        }

        Staff updatedStaff = staffRepository.save(staff);
        return convertToResponse(updatedStaff, "Staff updated successfully");
    }

    /**
     * Delete staff (soft delete by deactivating user account)
     */
    @Transactional
    public StaffResponse deleteStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));

        if (staff.getUser() != null) {
            staff.getUser().setIsDeleted(true);
            userRepository.save(staff.getUser());
        }

        return convertToResponse(staff, "Staff deactivated successfully");
    }

    /**
     * Get staff profile by username
     */
    public StaffResponse getStaffProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Staff staff = staffRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Staff profile not found for user: " + username));

        return convertToResponse(staff, null);
    }

    /**
     * Check if staff is owner of the account
     */
    public boolean isStaffOwner(Long staffId, String username) {
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (staff.isPresent() && staff.get().getUser() != null) {
            return staff.get().getUser().getUsername().equals(username);
        }
        return false;
    }

    /**
     * Get staff statistics
     */
    public StaffStatisticsResponse getStaffStatistics(Long staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));

        // TODO: Implement actual statistics calculation when Invoice relationships are available
        return StaffStatisticsResponse.builder()
                .staffId(staffId)
                .staffName(staff.getFullname())
                .position(staff.getPosition())
                .totalInvoicesProcessed(0)
                .totalRevenueProcessed(0.0)
                .averageInvoiceAmount(0.0)
                .invoicesThisMonth(0)
                .invoicesThisWeek(0)
                .invoicesToday(0)
                .monthlyBreakdown(java.util.List.of())
                .monthlyRevenueTarget(0.0)
                .monthlyRevenueActual(0.0)
                .performanceRate(0.0)
                .firstInvoiceDate("N/A")
                .lastInvoiceDate("N/A")
                .mostProductiveMonth("N/A")
                .leastProductiveMonth("N/A")
                .totalCustomersServed(0)
                .uniqueCustomersServed(0)
                .averageCustomersPerDay(0.0)
                .build();
    }

    /**
     * Convert Staff entity to StaffResponse
     */
    private StaffResponse convertToResponse(Staff staff, String message) {
        StaffResponse response = new StaffResponse();
        response.setId(staff.getId());
        response.setFullname(staff.getFullname());
        response.setPosition(staff.getPosition());
        response.setPhone(staff.getPhone());
        response.setEmail(staff.getEmail());
        response.setAddress(staff.getAddress());

        if (staff.getUser() != null) {
            response.setUserId(staff.getUser().getId());
            response.setUsername(staff.getUser().getUsername());
            response.setRole(staff.getUser().getRole());
            response.setIsDeleted(staff.getUser().getIsDeleted());
            response.setImgUrl(staff.getUser().getImgUrl());
        }

        response.setMessage(message);
        return response;
    }
}


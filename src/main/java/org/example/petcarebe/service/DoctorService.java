package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.doctor.CreateDoctorRequest;
import org.example.petcarebe.dto.request.doctor.UpdateDoctorRequest;
import org.example.petcarebe.dto.response.doctor.DoctorResponse;
import org.example.petcarebe.dto.response.doctor.DoctorStatisticsResponse;
import org.example.petcarebe.model.Doctor;
import org.example.petcarebe.model.User;
import org.example.petcarebe.repository.DoctorRepository;
import org.example.petcarebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Create a new doctor with user account
     */
    @Transactional
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (doctorRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Create user account first
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("DOCTOR");
        user.setIsDeleted(false);
        User savedUser = userRepository.save(user);

        // Create doctor profile
        Doctor doctor = new Doctor();
        doctor.setFullname(request.getFullname());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setGender(request.getGender());
        doctor.setBirthday(request.getBirthday());
        doctor.setUser(savedUser);

        Doctor savedDoctor = doctorRepository.save(doctor);
        return convertToResponse(savedDoctor, "Doctor created successfully");
    }

    /**
     * Get all doctors
     */
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctor -> convertToResponse(doctor, null))
                .collect(Collectors.toList());
    }

    /**
     * Get doctor by ID
     */
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        return convertToResponse(doctor, null);
    }

    /**
     * Update doctor information
     */
    @Transactional
    public DoctorResponse updateDoctor(Long id, UpdateDoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        // Check if email already exists (excluding current doctor)
        if (request.getEmail() != null && !request.getEmail().equals(doctor.getEmail())) {
            Optional<Doctor> existingDoctor = doctorRepository.findByEmail(request.getEmail());
            if (existingDoctor.isPresent() && !existingDoctor.get().getId().equals(id)) {
                throw new RuntimeException("Email already exists");
            }
        }

        // Update fields if provided
        if (request.getFullname() != null) {
            doctor.setFullname(request.getFullname());
        }
        if (request.getSpecialization() != null) {
            doctor.setSpecialization(request.getSpecialization());
        }
        if (request.getPhone() != null) {
            doctor.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            doctor.setEmail(request.getEmail());
        }
        if (request.getGender() != null) {
            doctor.setGender(request.getGender());
        }
        if (request.getBirthday() != null) {
            doctor.setBirthday(request.getBirthday());
        }

        Doctor updatedDoctor = doctorRepository.save(doctor);
        return convertToResponse(updatedDoctor, "Doctor updated successfully");
    }

    /**
     * Delete doctor (soft delete by deactivating user account)
     */
    @Transactional
    public DoctorResponse deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        // Soft delete by deactivating user account
        if (doctor.getUser() != null) {
            doctor.getUser().setIsDeleted(true);
            userRepository.save(doctor.getUser());
        }

        return convertToResponse(doctor, "Doctor deleted successfully");
    }

    /**
     * Get doctors by specialization
     */
    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationContainingIgnoreCase(specialization).stream()
                .map(doctor -> convertToResponse(doctor, null))
                .collect(Collectors.toList());
    }

    /**
     * Get active doctors only
     */
    public List<DoctorResponse> getActiveDoctors() {
        return doctorRepository.findByUser_IsDeletedFalse().stream()
                .map(doctor -> convertToResponse(doctor, null))
                .collect(Collectors.toList());
    }

    /**
     * Check if the authenticated user is the owner of the doctor profile
     */
    public boolean isDoctorOwner(Long doctorId, String username) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent() && doctor.get().getUser() != null) {
            return doctor.get().getUser().getUsername().equals(username);
        }
        return false;
    }

    /**
     * Get doctor by username
     */
    public DoctorResponse getDoctorByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Optional<Doctor> doctor = doctorRepository.findByUserId(user.get().getId());
            if (doctor.isPresent()) {
                return convertToResponse(doctor.get(), null);
            }
        }
        throw new RuntimeException("Doctor not found for username: " + username);
    }

    /**
     * Convert Doctor entity to DoctorResponse
     */
    private DoctorResponse convertToResponse(Doctor doctor, String message) {
        DoctorResponse response = new DoctorResponse();
        response.setId(doctor.getId());
        response.setFullname(doctor.getFullname());
        response.setSpecialization(doctor.getSpecialization());
        response.setPhone(doctor.getPhone());
        response.setEmail(doctor.getEmail());
        response.setGender(doctor.getGender());
        response.setBirthday(doctor.getBirthday());

        if (doctor.getUser() != null) {
            response.setUserId(doctor.getUser().getId());
            response.setUsername(doctor.getUser().getUsername());
            response.setUserRole(doctor.getUser().getRole());
            response.setIsDeleted(doctor.getUser().getIsDeleted());
        }

        response.setMessage(message);
        return response;
    }
}


package org.example.petcarebe.repository;

import org.example.petcarebe.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /**
     * Find doctor by email
     */
    Optional<Doctor> findByEmail(String email);

    /**
     * Find doctors by specialization (case insensitive)
     */
    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);

    /**
     * Find active doctors (user account not deleted)
     */
    List<Doctor> findByUser_IsDeletedFalse();

    /**
     * Find doctor by user ID
     */
    Optional<Doctor> findByUserId(Long userId);

    /**
     * Find doctors by phone
     */
    Optional<Doctor> findByPhone(String phone);

    /**
     * Find doctors by full name containing (case insensitive)
     */
    List<Doctor> findByFullnameContainingIgnoreCase(String fullname);

    /**
     * Count active doctors
     */
    @Query("SELECT COUNT(d) FROM Doctor d WHERE d.user.isDeleted = false")
    Long countActiveDoctors();

    /**
     * Find doctors by specialization and active status
     */
    @Query("SELECT d FROM Doctor d WHERE d.specialization LIKE %:specialization% AND d.user.isDeleted = false")
    List<Doctor> findActiveBySpecialization(@Param("specialization") String specialization);
}


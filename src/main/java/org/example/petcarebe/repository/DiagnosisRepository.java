package org.example.petcarebe.repository;

import org.example.petcarebe.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    /**
     * Find diagnoses by visit ID
     */
    List<Diagnosis> findByVisitId(Long visitId);

    /**
     * Find diagnoses by disease ID
     */
    List<Diagnosis> findByDiseaseId(Long diseaseId);

    /**
     * Find diagnoses by date range
     */
    List<Diagnosis> findByCreatedDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find diagnoses by specific date
     */
    List<Diagnosis> findByCreatedDate(LocalDate date);

    /**
     * Find diagnoses by pet ID (through visit)
     */
    @Query("SELECT d FROM Diagnosis d WHERE d.visit.pet.id = :petId")
    List<Diagnosis> findByPetId(@Param("petId") Long petId);

    /**
     * Find diagnoses by customer ID (through visit -> pet)
     */
    @Query("SELECT d FROM Diagnosis d WHERE d.visit.pet.customer.id = :customerId")
    List<Diagnosis> findByCustomerId(@Param("customerId") Long customerId);

    /**
     * Find diagnoses by doctor ID (through visit)
     */
    @Query("SELECT d FROM Diagnosis d WHERE d.visit.workSchedule.doctor.id = :doctorId")
    List<Diagnosis> findByDoctorId(@Param("doctorId") Long doctorId);

    /**
     * Count diagnoses by date
     */
    Long countByCreatedDate(LocalDate date);

    /**
     * Count diagnoses by date range
     */
    Long countByCreatedDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Count diagnoses by disease
     */
    Long countByDiseaseId(Long diseaseId);

    /**
     * Find most common diseases
     */
    @Query("SELECT d.disease.id, d.disease.name, COUNT(d) as count " +
           "FROM Diagnosis d GROUP BY d.disease.id, d.disease.name " +
           "ORDER BY count DESC")
    List<Object[]> findMostCommonDiseases();

    /**
     * Find most active doctors in diagnoses
     */
    @Query("SELECT d.visit.workSchedule.doctor.id, d.visit.workSchedule.doctor.fullname, d.visit.workSchedule.doctor.specialization, COUNT(d) as count " +
           "FROM Diagnosis d GROUP BY d.visit.workSchedule.doctor.id, d.visit.workSchedule.doctor.fullname, d.visit.workSchedule.doctor.specialization " +
           "ORDER BY count DESC")
    List<Object[]> findMostActiveDoctors();

    /**
     * Find pet type statistics
     */
    @Query("SELECT d.visit.pet.animalType.name, COUNT(d) as count " +
           "FROM Diagnosis d GROUP BY d.visit.pet.animalType.name " +
           "ORDER BY count DESC")
    List<Object[]> findPetTypeStatistics();

    /**
     * Search diagnoses by description
     */
    List<Diagnosis> findByDescriptionContainingIgnoreCase(String description);

    /**
     * Find all diagnoses ordered by created date desc
     */
    List<Diagnosis> findAllByOrderByCreatedDateDesc();
}


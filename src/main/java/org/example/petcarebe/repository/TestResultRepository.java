package org.example.petcarebe.repository;

import org.example.petcarebe.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult,Long> {

    /**
     * Find test results by visit ID
     */
    List<TestResult> findByVisitId(Long visitId);

    /**
     * Find test results by test type (case insensitive)
     */
    List<TestResult> findByTestTypeContainingIgnoreCase(String testType);

    /**
     * Find test results by date range
     */
    List<TestResult> findByCreatedDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find test results by specific date
     */
    List<TestResult> findByCreatedDate(LocalDate date);

    /**
     * Find test results by pet ID (through visit)
     */
    @Query("SELECT tr FROM TestResult tr WHERE tr.visit.pet.id = :petId")
    List<TestResult> findByPetId(@Param("petId") Long petId);

    /**
     * Find test results by customer ID (through visit -> pet)
     */
    @Query("SELECT tr FROM TestResult tr WHERE tr.visit.pet.customer.id = :customerId")
    List<TestResult> findByCustomerId(@Param("customerId") Long customerId);

    /**
     * Find test results by doctor ID (through visit)
     */
    @Query("SELECT tr FROM TestResult tr WHERE tr.visit.workSchedule.doctor.id = :doctorId")
    List<TestResult> findByDoctorId(@Param("doctorId") Long doctorId);

    /**
     * Count test results by date
     */
    Long countByCreatedDate(LocalDate date);

    /**
     * Count test results by date range
     */
    Long countByCreatedDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Count test results by test type
     */
    Long countByTestTypeContainingIgnoreCase(String testType);

    /**
     * Find most common test types
     */
    @Query("SELECT tr.testType, COUNT(tr) as count " +
           "FROM TestResult tr GROUP BY tr.testType " +
           "ORDER BY count DESC")
    List<Object[]> findMostCommonTestTypes();

    /**
     * Find most active doctors in test results
     */
    @Query("SELECT tr.visit.workSchedule.doctor.id, tr.visit.workSchedule.doctor.fullname, tr.visit.workSchedule.doctor.specialization, COUNT(tr) as count " +
           "FROM TestResult tr GROUP BY tr.visit.workSchedule.doctor.id, tr.visit.workSchedule.doctor.fullname, tr.visit.workSchedule.doctor.specialization " +
           "ORDER BY count DESC")
    List<Object[]> findMostActiveDoctors();

    /**
     * Find pet type statistics for test results
     */
    @Query("SELECT tr.visit.pet.animalType.name, COUNT(tr) as count " +
           "FROM TestResult tr GROUP BY tr.visit.pet.animalType.name " +
           "ORDER BY count DESC")
    List<Object[]> findPetTypeStatistics();

    /**
     * Search test results by result content
     */
    List<TestResult> findByResultContainingIgnoreCase(String result);

    /**
     * Search test results by notes
     */
    List<TestResult> findByNotesContainingIgnoreCase(String notes);

    /**
     * Find all test results ordered by created date desc
     */
    List<TestResult> findAllByOrderByCreatedDateDesc();

    /**
     * Count normal vs abnormal results (basic pattern matching)
     */
    @Query("SELECT " +
           "SUM(CASE WHEN LOWER(tr.result) LIKE '%normal%' OR LOWER(tr.result) LIKE '%negative%' OR LOWER(tr.result) LIKE '%within range%' THEN 1 ELSE 0 END) as normalCount, " +
           "SUM(CASE WHEN LOWER(tr.result) LIKE '%abnormal%' OR LOWER(tr.result) LIKE '%positive%' OR LOWER(tr.result) LIKE '%elevated%' OR LOWER(tr.result) LIKE '%high%' OR LOWER(tr.result) LIKE '%low%' THEN 1 ELSE 0 END) as abnormalCount " +
           "FROM TestResult tr")
    Object[] countNormalVsAbnormalResults();

    /**
     * Find test results with result patterns
     */
    @Query("SELECT " +
           "CASE " +
           "WHEN LOWER(tr.result) LIKE '%normal%' OR LOWER(tr.result) LIKE '%negative%' OR LOWER(tr.result) LIKE '%within range%' THEN 'Normal' " +
           "WHEN LOWER(tr.result) LIKE '%abnormal%' OR LOWER(tr.result) LIKE '%positive%' THEN 'Abnormal' " +
           "WHEN LOWER(tr.result) LIKE '%elevated%' OR LOWER(tr.result) LIKE '%high%' THEN 'Elevated' " +
           "WHEN LOWER(tr.result) LIKE '%low%' THEN 'Low' " +
           "ELSE 'Other' END as pattern, " +
           "COUNT(tr) as count " +
           "FROM TestResult tr " +
           "GROUP BY pattern " +
           "ORDER BY count DESC")
    List<Object[]> findResultPatterns();
}

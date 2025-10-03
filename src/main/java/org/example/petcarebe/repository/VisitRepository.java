package org.example.petcarebe.repository;

import org.example.petcarebe.model.Visit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    /**
     * Find visits by pet ID
     */
    List<Visit> findByPetId(Long petId);

    /**
     * Find visits by customer ID (through pet)
     */
    @Query("SELECT v FROM Visit v WHERE v.pet.customer.id = :customerId")
    List<Visit> findByCustomerId(@Param("customerId") Long customerId);

    /**
     * Find visits by doctor ID (through work schedule)
     */
    @Query("SELECT v FROM Visit v WHERE v.workSchedule.doctor.id = :doctorId")
    List<Visit> findByDoctorId(@Param("doctorId") Long doctorId);

    /**
     * Find visits by appointment ID
     */
    List<Visit> findByAppointmentId(Long appointmentId);

    /**
     * Find visits by clinic room ID
     */
    List<Visit> findByClinicRoomId(Long clinicRoomId);

    /**
     * Find visits by specific date
     */
    List<Visit> findByVisitDate(LocalDate visitDate);

    /**
     * Find visits by date range
     */
    List<Visit> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find visits by work schedule ID
     */
    List<Visit> findByWorkScheduleId(Long workScheduleId);

    /**
     * Find visits by reason (case insensitive)
     */
    List<Visit> findByReasonVisitContainingIgnoreCase(String reason);

    /**
     * Count visits by date
     */
    Long countByVisitDate(LocalDate visitDate);

    /**
     * Count visits by date range
     */
    Long countByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Count visits by doctor
     */
    @Query("SELECT COUNT(v) FROM Visit v WHERE v.workSchedule.doctor.id = :doctorId")
    Long countByDoctorId(@Param("doctorId") Long doctorId);

    /**
     * Count visits by pet
     */
    Long countByPetId(Long petId);

    /**
     * Find visits with diagnoses
     */
    @Query("SELECT DISTINCT v FROM Visit v JOIN Diagnosis d ON d.visit.id = v.id")
    List<Visit> findVisitsWithDiagnoses();

    /**
     * Find visits with test results
     */
    @Query("SELECT DISTINCT v FROM Visit v JOIN TestResult tr ON tr.visit.id = v.id")
    List<Visit> findVisitsWithTestResults();

    /**
     * Find recent visits (last N days)
     */
    @Query("SELECT v FROM Visit v WHERE v.visitDate >= :fromDate ORDER BY v.visitDate DESC, v.visitTime DESC")
    List<Visit> findRecentVisits(@Param("fromDate") LocalDate fromDate);

    /**
     * Find visits by pet and date range
     */
    @Query("SELECT v FROM Visit v WHERE v.pet.id = :petId AND v.visitDate BETWEEN :startDate AND :endDate ORDER BY v.visitDate DESC")
    List<Visit> findByPetIdAndDateRange(@Param("petId") Long petId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find visits by customer and date range
     */
    @Query("SELECT v FROM Visit v WHERE v.pet.customer.id = :customerId AND v.visitDate BETWEEN :startDate AND :endDate ORDER BY v.visitDate DESC")
    List<Visit> findByCustomerIdAndDateRange(@Param("customerId") Long customerId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find visits by doctor and date range
     */
    @Query("SELECT v FROM Visit v WHERE v.workSchedule.doctor.id = :doctorId AND v.visitDate BETWEEN :startDate AND :endDate ORDER BY v.visitDate DESC")
    List<Visit> findByDoctorIdAndDateRange(@Param("doctorId") Long doctorId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Get daily visit statistics
     */
    @Query("SELECT v.visitDate, COUNT(v) FROM Visit v WHERE v.visitDate BETWEEN :startDate AND :endDate GROUP BY v.visitDate ORDER BY v.visitDate")
    List<Object[]> getDailyVisitStatistics(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Get monthly visit statistics
     */
    @Query("SELECT YEAR(v.visitDate), MONTH(v.visitDate), COUNT(v) FROM Visit v WHERE v.visitDate BETWEEN :startDate AND :endDate GROUP BY YEAR(v.visitDate), MONTH(v.visitDate) ORDER BY YEAR(v.visitDate), MONTH(v.visitDate)")
    List<Object[]> getMonthlyVisitStatistics(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Get doctor performance statistics
     */
    @Query("SELECT v.workSchedule.doctor.id, v.workSchedule.doctor.fullname, v.workSchedule.doctor.specialization, COUNT(v) as visitCount " +
           "FROM Visit v WHERE v.visitDate BETWEEN :startDate AND :endDate " +
           "GROUP BY v.workSchedule.doctor.id, v.workSchedule.doctor.fullname, v.workSchedule.doctor.specialization " +
           "ORDER BY visitCount DESC")
    List<Object[]> getDoctorPerformanceStatistics(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Get most visited pets
     */
    @Query("SELECT v.pet.id, v.pet.name, v.pet.animalType.name, COUNT(v) as visitCount " +
           "FROM Visit v WHERE v.visitDate BETWEEN :startDate AND :endDate " +
           "GROUP BY v.pet.id, v.pet.name, v.pet.animalType.name " +
           "ORDER BY visitCount DESC")
    List<Object[]> getMostVisitedPets(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Get clinic room utilization
     */
    @Query("SELECT v.clinicRoom.id, v.clinicRoom.name, COUNT(v) as visitCount " +
           "FROM Visit v WHERE v.visitDate BETWEEN :startDate AND :endDate " +
           "GROUP BY v.clinicRoom.id, v.clinicRoom.name " +
           "ORDER BY visitCount DESC")
    List<Object[]> getClinicRoomUtilization(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(v) FROM Visit v " +
            "WHERE v.visitDate = :queryDate AND v.pet IS NOT NULL")
    Integer getPetsNeedExamToday(@Param("queryDate") LocalDate now);

    Integer getVisitByVisitDateAndVisitTime(LocalDate visitDate, LocalTime visitTime, Sort sort);

    Integer getVisitByVisitDate(LocalDate visitDate, Sort sort);

    /**
     * Find visits by pet ID and visit date
     */
    @Query("SELECT v FROM Visit v WHERE v.pet.id = :petId AND v.visitDate = :visitDate")
    List<Visit> findByPetIdAndVisitDate(@Param("petId") Long petId, @Param("visitDate") LocalDate visitDate);
}


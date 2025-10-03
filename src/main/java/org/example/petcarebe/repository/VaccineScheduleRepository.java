package org.example.petcarebe.repository;

import org.example.petcarebe.model.VaccineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccineScheduleRepository extends JpaRepository<VaccineSchedule,Long> {

    /**
     * Find vaccine schedules by pet ID
     */
    List<VaccineSchedule> findByPetId(Long petId);

    /**
     * Find vaccine schedules by vaccine ID
     */
    List<VaccineSchedule> findByVaccineId(Long vaccineId);

    /**
     * Find vaccine schedules by status
     */
    List<VaccineSchedule> findByStatus(String status);

    /**
     * Find vaccine schedules by scheduled date
     */
    List<VaccineSchedule> findByScheduledDate(LocalDate scheduledDate);

    /**
     * Find vaccine schedules by date range
     */
    List<VaccineSchedule> findByScheduledDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find vaccine schedules by pet and status
     */
    List<VaccineSchedule> findByPetIdAndStatus(Long petId, String status);

    /**
     * Find vaccine schedules by vaccine and status
     */
    List<VaccineSchedule> findByVaccineIdAndStatus(Long vaccineId, String status);

    /**
     * Find overdue vaccine schedules (scheduled date before today and status is SCHEDULED)
     */
    @Query("SELECT vs FROM VaccineSchedule vs WHERE vs.scheduledDate < :currentDate AND vs.status = 'SCHEDULED'")
    List<VaccineSchedule> findOverdueSchedules(@Param("currentDate") LocalDate currentDate);

    /**
     * Find upcoming vaccine schedules (scheduled date within next N days and status is SCHEDULED)
     */
    @Query("SELECT vs FROM VaccineSchedule vs WHERE vs.scheduledDate BETWEEN :startDate AND :endDate AND vs.status = 'SCHEDULED'")
    List<VaccineSchedule> findUpcomingSchedules(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find vaccine schedules by customer ID (through pet)
     */
    @Query("SELECT vs FROM VaccineSchedule vs WHERE vs.pet.customer.id = :customerId")
    List<VaccineSchedule> findByCustomerId(@Param("customerId") Long customerId);

    /**
     * Find vaccine schedules by pet and date range
     */
    @Query("SELECT vs FROM VaccineSchedule vs WHERE vs.pet.id = :petId AND vs.scheduledDate BETWEEN :startDate AND :endDate ORDER BY vs.scheduledDate")
    List<VaccineSchedule> findByPetIdAndDateRange(@Param("petId") Long petId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find vaccine schedules by customer and date range
     */
    @Query("SELECT vs FROM VaccineSchedule vs WHERE vs.pet.customer.id = :customerId AND vs.scheduledDate BETWEEN :startDate AND :endDate ORDER BY vs.scheduledDate")
    List<VaccineSchedule> findByCustomerIdAndDateRange(@Param("customerId") Long customerId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Count vaccine schedules by status
     */
    Long countByStatus(String status);

    /**
     * Count vaccine schedules by date
     */
    Long countByScheduledDate(LocalDate scheduledDate);

    /**
     * Count vaccine schedules by date range
     */
    Long countByScheduledDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Count overdue schedules
     */
    @Query("SELECT COUNT(vs) FROM VaccineSchedule vs WHERE vs.scheduledDate < :currentDate AND vs.status = 'SCHEDULED'")
    Long countOverdueSchedules(@Param("currentDate") LocalDate currentDate);

    /**
     * Count upcoming schedules
     */
    @Query("SELECT COUNT(vs) FROM VaccineSchedule vs WHERE vs.scheduledDate BETWEEN :startDate AND :endDate AND vs.status = 'SCHEDULED'")
    Long countUpcomingSchedules(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Get vaccine schedule statistics by status
     */
    @Query("SELECT vs.status, COUNT(vs) FROM VaccineSchedule vs GROUP BY vs.status")
    List<Object[]> getScheduleStatisticsByStatus();

    /**
     * Get most scheduled vaccines
     */
    @Query("SELECT vs.vaccine.id, vs.vaccine.name, COUNT(vs) as scheduleCount " +
           "FROM VaccineSchedule vs GROUP BY vs.vaccine.id, vs.vaccine.name " +
           "ORDER BY scheduleCount DESC")
    List<Object[]> getMostScheduledVaccines();

    /**
     * Get pets with most vaccine schedules
     */
    @Query("SELECT vs.pet.id, vs.pet.name, vs.pet.animalType.name, COUNT(vs) as scheduleCount " +
           "FROM VaccineSchedule vs GROUP BY vs.pet.id, vs.pet.name, vs.pet.animalType.name " +
           "ORDER BY scheduleCount DESC")
    List<Object[]> getPetsWithMostSchedules();

    /**
     * Get daily schedule statistics
     */
    @Query("SELECT vs.scheduledDate, COUNT(vs) FROM VaccineSchedule vs WHERE vs.scheduledDate BETWEEN :startDate AND :endDate GROUP BY vs.scheduledDate ORDER BY vs.scheduledDate")
    List<Object[]> getDailyScheduleStatistics(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Get monthly schedule statistics
     */
    @Query("SELECT YEAR(vs.scheduledDate), MONTH(vs.scheduledDate), COUNT(vs) FROM VaccineSchedule vs WHERE vs.scheduledDate BETWEEN :startDate AND :endDate GROUP BY YEAR(vs.scheduledDate), MONTH(vs.scheduledDate) ORDER BY YEAR(vs.scheduledDate), MONTH(vs.scheduledDate)")
    List<Object[]> getMonthlyScheduleStatistics(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find schedules that need reminder (scheduled date within next N days)
     */
    @Query("SELECT vs FROM VaccineSchedule vs WHERE vs.scheduledDate BETWEEN :startDate AND :endDate AND vs.status = 'SCHEDULED' ORDER BY vs.scheduledDate")
    List<VaccineSchedule> findSchedulesNeedingReminder(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Check if pet has existing schedule for same vaccine
     */
    @Query("SELECT COUNT(vs) > 0 FROM VaccineSchedule vs WHERE vs.pet.id = :petId AND vs.vaccine.id = :vaccineId AND vs.status IN ('SCHEDULED', 'COMPLETED')")
    Boolean existsByPetIdAndVaccineId(@Param("petId") Long petId, @Param("vaccineId") Long vaccineId);

    /**
     * Find all schedules ordered by date and status
     */
    @Query("SELECT vs FROM VaccineSchedule vs ORDER BY vs.scheduledDate DESC, vs.status")
    List<VaccineSchedule> findAllOrderByDateAndStatus();

    /**
     * Find schedules by pet ordered by date
     */
    @Query("SELECT vs FROM VaccineSchedule vs WHERE vs.pet.id = :petId ORDER BY vs.scheduledDate DESC")
    List<VaccineSchedule> findByPetIdOrderByDate(@Param("petId") Long petId);

    /**
     * Find vaccine schedules by pet ID and scheduled date
     */
    @Query("SELECT vs FROM VaccineSchedule vs WHERE vs.pet.id = :petId AND vs.scheduledDate = :scheduledDate")
    List<VaccineSchedule> findByPetIdAndScheduledDate(@Param("petId") Long petId, @Param("scheduledDate") LocalDate scheduledDate);
}

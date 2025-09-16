package org.example.petcarebe.repository;

import org.example.petcarebe.model.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

    /**
     * Find work schedules by doctor ID
     */
    List<WorkSchedule> findByDoctorId(Long doctorId);

    /**
     * Find work schedules by doctor ID ordered by day of week
     */
    @Query("SELECT ws FROM WorkSchedule ws WHERE ws.doctor.id = :doctorId ORDER BY ws.workDate ASC")
    List<WorkSchedule> findByDoctorIdOrderByDayOfWeek(@Param("doctorId") Long doctorId);

    /**
     * Find work schedule by doctor ID and work date
     */
    Optional<WorkSchedule> findByDoctorIdAndWorkDate(Long doctorId, LocalDate workDate);



    /**
     * Find work schedules by time range
     */
    @Query("SELECT ws FROM WorkSchedule ws WHERE ws.startTime <= :endTime AND ws.endTime >= :startTime")
    List<WorkSchedule> findByTimeRange(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);



    /**
     * Count work schedules by doctor
     */
    @Query("SELECT COUNT(ws) FROM WorkSchedule ws WHERE ws.doctor.id = :doctorId")
    Long countByDoctorId(@Param("doctorId") Long doctorId);

    /**
     * Find work schedules with overlapping time for same doctor on same date
     */
    @Query("SELECT ws FROM WorkSchedule ws WHERE ws.doctor.id = :doctorId AND ws.workDate = :workDate " +
           "AND ws.id != :excludeId AND ((ws.startTime <= :endTime AND ws.endTime >= :startTime))")
    List<WorkSchedule> findOverlappingSchedules(@Param("doctorId") Long doctorId,
                                               @Param("workDate") LocalDate workDate,
                                               @Param("startTime") LocalTime startTime,
                                               @Param("endTime") LocalTime endTime,
                                               @Param("excludeId") Long excludeId);

    /**
     * Find work schedules by date range
     */
    @Query("SELECT ws FROM WorkSchedule ws WHERE ws.workDate BETWEEN :startDate AND :endDate ORDER BY ws.workDate, ws.startTime")
    List<WorkSchedule> findByWorkDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find work schedules by doctor and date range
     */
    @Query("SELECT ws FROM WorkSchedule ws WHERE ws.doctor.id = :doctorId AND ws.workDate BETWEEN :startDate AND :endDate ORDER BY ws.workDate, ws.startTime")
    List<WorkSchedule> findByDoctorIdAndWorkDateBetween(@Param("doctorId") Long doctorId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find work schedules by specific work date
     */
    @Query("SELECT ws FROM WorkSchedule ws WHERE ws.workDate = :workDate ORDER BY ws.startTime")
    List<WorkSchedule> findByWorkDate(@Param("workDate") LocalDate workDate);

    /**
     * Find all work schedules ordered by doctor name and work date
     */
    @Query("SELECT ws FROM WorkSchedule ws JOIN ws.doctor d ORDER BY d.fullname, ws.workDate, ws.startTime")
    List<WorkSchedule> findAllOrderByDoctorAndDate();

//    Optional<WorkSchedule> findByDoctorIdAndWorkDate(Long doctorId, LocalDate workDate);
}


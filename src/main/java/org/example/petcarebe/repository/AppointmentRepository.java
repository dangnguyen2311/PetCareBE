package org.example.petcarebe.repository;

import org.example.petcarebe.enums.AppointmentStatus;
import org.example.petcarebe.model.Appointment;
import org.example.petcarebe.model.Customer;
import org.example.petcarebe.model.WorkSchedule;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT COUNT(*) FROM Appointment p WHERE p.workSchedule.id = :workScheduleId")
    int getAppointmentNumberByWorkSchedule(@Param("workScheduleId") Long workScheduleId);

    List<Appointment> findAllByCustomer(Customer customer);

    List<Appointment> findAllByStatus(AppointmentStatus status);

    Integer getAppointmentsByAppointmentDate(LocalDate appointmentDate, Sort sort);

    /**
     * Find appointments by pet ID and appointment date
     */
//    @Query("SELECT a FROM Appointment a WHERE a.pet.id = :petId AND a.appointmentDate = :appointmentDate")
//    List<Appointment> findByPetIdAndAppointmentDate(@Param("petId") Long petId, @Param("appointmentDate") LocalDate appointmentDate);
}


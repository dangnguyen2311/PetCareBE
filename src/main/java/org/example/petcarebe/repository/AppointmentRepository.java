package org.example.petcarebe.repository;

import org.example.petcarebe.model.Appointment;
import org.example.petcarebe.model.Customer;
import org.example.petcarebe.model.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT COUNT(*) FROM Appointment p WHERE p.workSchedule.id = :workScheduleId")
    int getAppointmentNumberByWorkSchedule(@Param("workScheduleId") Long workScheduleId);

    List<Appointment> findAllByCustomer(Customer customer);
}


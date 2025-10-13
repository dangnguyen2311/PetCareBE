package org.example.petcarebe.repository;

import org.example.petcarebe.model.Doctor;
import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.Prescription;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription,Long> {

    Optional<Prescription> findByInvoice(Invoice invoice);

    Integer getPrescriptionsByCreatedDate(LocalDate createdDate, Sort sort);

    List<Prescription> findAllByDoctor(Doctor doctorById);

    List<Prescription> findAllByDoctorAndCreatedDate(Doctor doctor, LocalDate createdDate, Sort sort);

    Optional<Prescription> findByInvoiceId(Long invoiceId);
}

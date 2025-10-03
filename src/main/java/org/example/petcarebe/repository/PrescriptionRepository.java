package org.example.petcarebe.repository;

import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.Prescription;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription,Long> {

    Optional<Prescription> findByInvoice(Invoice invoice);

    Integer getPrescriptionsByCreatedDate(LocalDate createdDate, Sort sort);
}

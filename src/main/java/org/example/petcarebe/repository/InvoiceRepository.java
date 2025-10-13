package org.example.petcarebe.repository;

import org.example.petcarebe.enums.InvoiceStatus;
import org.example.petcarebe.model.Customer;
import org.example.petcarebe.model.Invoice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> getInvoicesByCustomer(Customer customer);

    List<Invoice> findAllByStatusAndCreatedDateBetween(InvoiceStatus status, LocalDate createdDateAfter, LocalDate createdDateBefore, Sort sort);

    List<Invoice> findAllByCreatedDateBetween(LocalDate createdDateAfter, LocalDate createdDateBefore, Sort sort);
}


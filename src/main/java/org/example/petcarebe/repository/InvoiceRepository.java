package org.example.petcarebe.repository;

import org.example.petcarebe.model.Customer;
import org.example.petcarebe.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> getInvoicesByCustomer(Customer customer);
}


package org.example.petcarebe.repository;

import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.ServiceInInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceInInvoiceRepository extends JpaRepository<ServiceInInvoice,Long> {
    List<ServiceInInvoice> findAllByInvoice(Invoice invoice);
}

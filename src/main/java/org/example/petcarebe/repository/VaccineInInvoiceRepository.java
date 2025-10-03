package org.example.petcarebe.repository;

import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.VaccineInInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VaccineInInvoiceRepository extends JpaRepository<VaccineInInvoice,Long> {
    List<VaccineInInvoice> findAllByInvoice(Invoice invoice);
}

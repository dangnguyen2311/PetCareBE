package org.example.petcarebe.repository;

import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.ServicePackageInInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicePackageInInvoiceRepository extends JpaRepository<ServicePackageInInvoice,Long> {
    List<ServicePackageInInvoice> findAllByInvoice(Invoice invoice);
}

package org.example.petcarebe.repository;

import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.ProductInInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInInvoiceRepository extends JpaRepository<ProductInInvoice,Long> {
    List<ProductInInvoice> findAllByInvoice(Invoice invoice);
}

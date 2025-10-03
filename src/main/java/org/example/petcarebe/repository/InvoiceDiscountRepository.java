package org.example.petcarebe.repository;

import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.InvoiceDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceDiscountRepository extends JpaRepository<InvoiceDiscount, Long> {
    List<InvoiceDiscount> findAllByInvoice(Invoice invoice);
}

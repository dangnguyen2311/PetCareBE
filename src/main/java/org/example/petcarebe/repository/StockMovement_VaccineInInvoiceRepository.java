package org.example.petcarebe.repository;

import org.example.petcarebe.model.StockMovement_VaccineInInvoice;
import org.example.petcarebe.model.VaccineInInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StockMovement_VaccineInInvoiceRepository extends JpaRepository<StockMovement_VaccineInInvoice, Integer> {
    List<StockMovement_VaccineInInvoice> findAllByVaccineInInvoice(VaccineInInvoice vaccineInInvoice);
}

package org.example.petcarebe.repository;

import org.example.petcarebe.model.ProductInInvoice;
import org.example.petcarebe.model.StockMovement_ProductInInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StockMovement_ProductInInvoiceRepository extends JpaRepository<StockMovement_ProductInInvoice, Integer> {
    List<StockMovement_ProductInInvoice> findAllByProductInInvoice(ProductInInvoice productInInvoice);
}

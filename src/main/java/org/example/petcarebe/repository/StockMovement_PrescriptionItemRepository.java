package org.example.petcarebe.repository;

import org.example.petcarebe.model.PrescriptionItem;
import org.example.petcarebe.model.StockMovement_PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockMovement_PrescriptionItemRepository extends JpaRepository<StockMovement_PrescriptionItem, Long> {
    List<StockMovement_PrescriptionItem> findAllByPrescriptionItem(PrescriptionItem prescriptionItem);

    StockMovement_PrescriptionItem findByPrescriptionItem(PrescriptionItem prescriptionItem);

    StockMovement_PrescriptionItem findByPrescriptionItemAndStockMovement_Quantity(PrescriptionItem prescriptionItem, Integer stockMovementQuantity);

    Optional<StockMovement_PrescriptionItem> findTopByPrescriptionItemOrderByCreatedAtDesc(PrescriptionItem prescriptionItem);
}

package org.example.petcarebe.repository;

import org.example.petcarebe.model.PrescriptionItem;
import org.example.petcarebe.model.StockMovement_PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovement_PrescriptionItemRepository extends JpaRepository<StockMovement_PrescriptionItem, Long> {
    List<StockMovement_PrescriptionItem> findAllByPrescriptionItem(PrescriptionItem prescriptionItem);
}

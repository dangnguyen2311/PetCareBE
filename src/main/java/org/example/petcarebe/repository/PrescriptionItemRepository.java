package org.example.petcarebe.repository;

import org.example.petcarebe.model.Prescription;
import org.example.petcarebe.model.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {
    List<PrescriptionItem> findAllByPrescription(Prescription prescription);

    List<PrescriptionItem> findByPrescriptionId(Long prescriptionId);

    @Query("SELECT COUNT(pi) FROM PrescriptionItem pi WHERE pi.prescription.id = :prescriptionId")
    Integer countByPrescriptionId(@Param("prescriptionId") Long prescriptionId);

    @Query("SELECT SUM(pi.price + (pi.price * pi.taxPercent / 100) - pi.promotionAmount) FROM PrescriptionItem pi WHERE pi.prescription.id = :prescriptionId")
    Double calculateTotalAmountByPrescriptionId(@Param("prescriptionId") Long prescriptionId);

    @Modifying
    @Query("DELETE FROM PrescriptionItem pi WHERE pi.prescription.id = :prescriptionId")
    void deleteByPrescriptionId(@Param("prescriptionId") Long prescriptionId);
}

package org.example.petcarebe.repository;

import org.example.petcarebe.model.Prescription;
import org.example.petcarebe.model.PrescriptionItem;
import org.example.petcarebe.model.dto.PrescriptionItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {
//
//    public record PrescriptionItemDTO(
//            Long id,
//            String dosage,
//            String duration,
//            String instruction,
//            Double price,
//            Integer quantity,
//            Double taxPercent,
//            Double promotionAmount
//    ) {}

    List<PrescriptionItem> findAllByPrescription(Prescription prescription);

    @Query("SELECT new org.example.petcarebe.model.dto.PrescriptionItemDTO(pi.id, pi.dosage, pi.duration, pi.instruction, pi.price, pi.quantity, pi.taxPercent, pi.promotionAmount) FROM PrescriptionItem pi WHERE pi.prescription.id = :prescriptionId")
    List<PrescriptionItemDTO> findAllByPrescriptionId(@Param("prescriptionId") Long prescriptionId);

    List<PrescriptionItem> findByPrescriptionId(Long prescriptionId);

    @Query("SELECT COUNT(pi) FROM PrescriptionItem pi WHERE pi.prescription.id = :prescriptionId")
    Integer countByPrescriptionId(@Param("prescriptionId") Long prescriptionId);

    @Query("SELECT SUM(pi.price + (pi.price * pi.taxPercent / 100) - pi.promotionAmount) FROM PrescriptionItem pi WHERE pi.prescription.id = :prescriptionId")
    Double calculateTotalAmountByPrescriptionId(@Param("prescriptionId") Long prescriptionId);

    @Modifying
    @Query("DELETE FROM PrescriptionItem pi WHERE pi.prescription.id = :prescriptionId")
    void deleteByPrescriptionId(@Param("prescriptionId") Long prescriptionId);
}

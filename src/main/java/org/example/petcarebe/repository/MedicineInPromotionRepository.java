package org.example.petcarebe.repository;

import org.example.petcarebe.model.Medicine;
import org.example.petcarebe.model.MedicineInPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineInPromotionRepository extends JpaRepository<MedicineInPromotion,Long> {
    List<MedicineInPromotion> findAllByMedicine(Medicine medicine);
}

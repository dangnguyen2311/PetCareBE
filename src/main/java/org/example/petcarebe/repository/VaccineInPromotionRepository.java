package org.example.petcarebe.repository;

import org.example.petcarebe.model.Vaccine;
import org.example.petcarebe.model.VaccineInPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VaccineInPromotionRepository extends JpaRepository<VaccineInPromotion,Long> {
    Optional<VaccineInPromotion> findByVaccine(Vaccine vaccine);
}

package org.example.petcarebe.repository;

import org.example.petcarebe.model.ServicePackage;
import org.example.petcarebe.model.ServicePackageInPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicePackageInPromotionRepository extends JpaRepository<ServicePackageInPromotion,Long> {
    Optional<ServicePackageInPromotion> findByServicePackage(ServicePackage servicePackage);
}

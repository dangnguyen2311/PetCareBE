package org.example.petcarebe.repository;

import org.example.petcarebe.model.Service;
import org.example.petcarebe.model.ServiceInPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceInPromotionRepository extends JpaRepository<ServiceInPromotion,Long> {
    Optional<ServiceInPromotion> findByService(Service service);
}

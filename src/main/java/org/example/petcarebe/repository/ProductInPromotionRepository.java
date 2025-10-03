package org.example.petcarebe.repository;

import org.example.petcarebe.model.Product;
import org.example.petcarebe.model.ProductInPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInPromotionRepository extends JpaRepository<ProductInPromotion,Long> {
    Optional<ProductInPromotion> findByProduct(Product product);
}

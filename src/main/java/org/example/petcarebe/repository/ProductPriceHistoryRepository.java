package org.example.petcarebe.repository;

import jakarta.transaction.Transactional;
import org.example.petcarebe.model.ProductPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistory,Long> {

    @Transactional
    @Modifying
    @Query("UPDATE ProductPriceHistory p SET p.status = 'INACTIVE', p.endDate = :endDate WHERE p.status = 'ACTIVE'")
    void deactivateAllActiveRecord(@Param("endDate") LocalDate endDate);
}

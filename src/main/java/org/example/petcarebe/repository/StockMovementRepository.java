package org.example.petcarebe.repository;

import org.example.petcarebe.dto.response.stockmovement.StockMovementResponse;
import org.example.petcarebe.model.StockMovement;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findAllByMovementDateBetween(LocalDateTime movementDateAfter, LocalDateTime movementDateBefore, Sort sort);
}

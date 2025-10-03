package org.example.petcarebe.repository;

import jakarta.transaction.Transactional;
import org.example.petcarebe.model.MedicinePriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicinePriceHistoryRepository extends JpaRepository<MedicinePriceHistory,Long> {
    List<MedicinePriceHistory> getAllByStatus(String status);

    @Modifying
    @Transactional
    @Query("UPDATE MedicinePriceHistory m SET m.status = 'INACTIVE', m.endDate = :endDate WHERE m.status = 'ACTIVE'")
    void deactiveAllActiveRecord(@Param("endDate") LocalDate endDate);

    Optional<MedicinePriceHistory> findByStatus(String status);
}

package org.example.petcarebe.repository;

import jakarta.transaction.Transactional;
import org.example.petcarebe.model.Service;
import org.example.petcarebe.model.ServicePriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ServicePriceHistoryRepository extends JpaRepository<ServicePriceHistory,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE ServicePriceHistory s SET s.status = 'INACTIVE', s.endDate = :endDate WHERE s.status = 'ACTIVE' ")
    void deactiveAllActiveRecord(@Param("endDate") LocalDate endDate);

    Optional<ServicePriceHistory> findByStatus(String status);

    Optional<ServicePriceHistory> findByServiceAndStatus(Service service, String status);
}

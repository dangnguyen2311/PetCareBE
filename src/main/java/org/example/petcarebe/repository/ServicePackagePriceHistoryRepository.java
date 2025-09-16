package org.example.petcarebe.repository;

import jakarta.transaction.Transactional;
import jdk.jfr.Registered;
import org.example.petcarebe.model.ServicePackagePriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

@Registered
public interface ServicePackagePriceHistoryRepository extends JpaRepository<ServicePackagePriceHistory,Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ServicePackagePriceHistory sp SET sp.status = 'INACTIVE', sp.endDate = :endDate WHERE sp.status = 'ACTIVE'")
    void deactiveAllActiveRecord(LocalDate endDate);
}

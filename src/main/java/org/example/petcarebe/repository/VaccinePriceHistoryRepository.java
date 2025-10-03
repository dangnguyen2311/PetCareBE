package org.example.petcarebe.repository;

import jakarta.transaction.Transactional;
import org.example.petcarebe.model.Vaccine;
import org.example.petcarebe.model.VaccinePriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VaccinePriceHistoryRepository extends JpaRepository<VaccinePriceHistory,Long> {

    @Modifying
    @Transactional
    @Query("UPDATE VaccinePriceHistory vp SET vp.status = 'INACTIVE', vp.endDate = :endDate WHERE vp.status = 'ACTIVE'")
    void deactiveAllActiveRecord(@Param("endDate") LocalDate endDate);

    Optional<VaccinePriceHistory> findByVaccineAndStatus(Vaccine vaccine, String status);
}

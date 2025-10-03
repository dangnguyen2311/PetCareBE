package org.example.petcarebe.repository;

import org.example.petcarebe.model.Pet;
import org.example.petcarebe.model.VaccinationRecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
public interface   VaccinationRecordRepository extends JpaRepository<VaccinationRecord,Integer> {
    Integer getVaccinationRecordsByStatus(String status, Sort sort);

    List<VaccinationRecord> findAllByPet(Pet pet);
}

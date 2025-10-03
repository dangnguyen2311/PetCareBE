package org.example.petcarebe.repository;

import org.example.petcarebe.model.MedicalRecord;
import org.example.petcarebe.model.Pet;
import org.example.petcarebe.model.PetWeightRecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PetWeightRecordRepository extends JpaRepository<PetWeightRecord, Long> {
    List<PetWeightRecord> findAllByMedicalRecord(MedicalRecord medicalRecord, Sort sort); //CrudRepository

    List<PetWeightRecord> findAllByMedicalRecordAndRecordDate(MedicalRecord medicalRecord, LocalDate recordDate, Sort sort);
}

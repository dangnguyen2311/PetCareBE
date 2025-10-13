package org.example.petcarebe.repository;

import org.example.petcarebe.dto.response.pet.PetFoodRecordResponse;
import org.example.petcarebe.model.MedicalRecord;
import org.example.petcarebe.model.PetFoodRecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PetFoodRecordRepository extends JpaRepository<PetFoodRecord,Long> {

    List<PetFoodRecord> findAllByMedicalRecord(MedicalRecord medicalRecord, Sort sort);

    List<PetFoodRecord> findAllByMedicalRecordAndRecordDate(MedicalRecord medicalRecord, LocalDate recordDate, Sort sort);

}

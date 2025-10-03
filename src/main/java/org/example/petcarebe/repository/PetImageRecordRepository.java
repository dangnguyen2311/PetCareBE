package org.example.petcarebe.repository;

import org.example.petcarebe.model.MedicalRecord;
import org.example.petcarebe.model.PetImageRecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PetImageRecordRepository extends JpaRepository<PetImageRecord, Long> {
    List<PetImageRecord> findAllByMedicalRecord(MedicalRecord medicalRecord, Sort sort); //extends CrudRepository<PetImageRecord, Long>

    List<PetImageRecord> findAllByMedicalRecordAndRecordDate(MedicalRecord medicalRecord, LocalDate recordDate, Sort sort);
}

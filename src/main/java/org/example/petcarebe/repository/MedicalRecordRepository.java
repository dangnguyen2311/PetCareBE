package org.example.petcarebe.repository;

import org.example.petcarebe.model.MedicalRecord;
import org.example.petcarebe.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByPet(Pet pet);
}


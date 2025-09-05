package org.example.petcarebe.repository;

import org.example.petcarebe.model.Pet;
import org.example.petcarebe.model.PetWeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetWeightRecordRepository extends JpaRepository<PetWeightRecord, Long> { //CrudRepository
}

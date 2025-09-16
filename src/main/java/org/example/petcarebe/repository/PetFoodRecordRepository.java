package org.example.petcarebe.repository;

import org.example.petcarebe.model.PetFoodRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetFoodRecordRepository extends JpaRepository<PetFoodRecord,Long> {

}

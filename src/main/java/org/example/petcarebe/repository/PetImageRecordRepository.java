package org.example.petcarebe.repository;

import org.example.petcarebe.model.PetImageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetImageRecordRepository extends JpaRepository<PetImageRecord, Long> { //extends CrudRepository<PetImageRecord, Long>
}

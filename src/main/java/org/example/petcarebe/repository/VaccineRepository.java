package org.example.petcarebe.repository;

import org.example.petcarebe.model.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    List<Vaccine> findAllByIsDeleted(Boolean isDeleted);
}


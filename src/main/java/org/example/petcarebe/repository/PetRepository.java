package org.example.petcarebe.repository;

import org.example.petcarebe.model.Pet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByCustomerId(Long customerId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Pet p SET p.isDeleted = true WHERE p.id = :petId")
    void deletePet(@Param("petId") Long petId);

    List<Pet> findAllByIsDeleted(Boolean isDeleted, Sort sort);
}


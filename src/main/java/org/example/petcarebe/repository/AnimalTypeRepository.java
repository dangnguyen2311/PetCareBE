package org.example.petcarebe.repository;

import org.example.petcarebe.model.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimalTypeRepository extends JpaRepository<AnimalType, Long> {
//    AnimalType findByName(String name);

    AnimalType findAnimalTypeByName(String name);
//    Optional<AnimalType> findByName(String name);

}


package org.example.petcarebe.repository;

import org.example.petcarebe.model.ClinicRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ClinicRoomRepository extends JpaRepository<ClinicRoom, Long> {
    List<ClinicRoom> findAllByIsDeleted(Boolean isDeleted);
}


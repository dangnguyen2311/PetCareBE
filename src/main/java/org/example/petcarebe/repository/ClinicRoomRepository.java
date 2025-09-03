package org.example.petcarebe.repository;

import org.example.petcarebe.model.ClinicRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRoomRepository extends JpaRepository<ClinicRoom, Long> {
}


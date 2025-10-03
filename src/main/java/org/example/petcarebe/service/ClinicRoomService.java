package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.clinicroom.CreateClinicRoomRequest;
import org.example.petcarebe.dto.request.clinicroom.UpdateClinicRoomRequest;
import org.example.petcarebe.dto.response.clinicroom.ClinicRoomResponse;
import org.example.petcarebe.model.ClinicRoom;
import org.example.petcarebe.repository.ClinicRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClinicRoomService {

    @Autowired
    private ClinicRoomRepository clinicRoomRepository;

    public ClinicRoomResponse createClinicRoom(CreateClinicRoomRequest request) {
        ClinicRoom clinicRoom = new ClinicRoom();
        clinicRoom.setName(request.getName());
        clinicRoom.setType(request.getType());
        clinicRoom.setStatus("ACTIVE");
        clinicRoom.setIsDeleted(false);
        clinicRoom.setAddress(request.getAddress());

        ClinicRoom savedClinicRoom = clinicRoomRepository.save(clinicRoom);
        return convertToResponse(savedClinicRoom);
    }

    public List<ClinicRoomResponse> getAllClinicRooms() {
        return clinicRoomRepository.findAllByIsDeleted(false).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ClinicRoomResponse getClinicRoomById(Long id) {
        ClinicRoom clinicRoom = clinicRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClinicRoom not found with id: " + id));
        return convertToResponse(clinicRoom);
    }

    public ClinicRoomResponse updateClinicRoom(Long id, UpdateClinicRoomRequest request) {
        ClinicRoom clinicRoom = clinicRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClinicRoom not found with id: " + id));

        clinicRoom.setName(request.getName());
        clinicRoom.setType(request.getType());
        clinicRoom.setStatus(request.getStatus());

        ClinicRoom updatedClinicRoom = clinicRoomRepository.save(clinicRoom);
        return convertToResponse(updatedClinicRoom);
    }

    public void deleteClinicRoom(Long id) {
        ClinicRoom clinicRoom = clinicRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClinicRoom not found with id: " + id));
        clinicRoom.setIsDeleted(true);
        clinicRoom.setType("INACTIVE");
        clinicRoomRepository.save(clinicRoom);
    }

    private ClinicRoomResponse convertToResponse(ClinicRoom clinicRoom) {
        return new ClinicRoomResponse(
                clinicRoom.getId(),
                clinicRoom.getName(),
                clinicRoom.getType(),
                clinicRoom.getStatus(),
                clinicRoom.getAddress()
        );
    }
}


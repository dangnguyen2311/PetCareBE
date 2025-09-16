package org.example.petcarebe.controller;

import org.example.petcarebe.dto.request.clinicroom.CreateClinicRoomRequest;
import org.example.petcarebe.dto.request.clinicroom.UpdateClinicRoomRequest;
import org.example.petcarebe.dto.response.clinicroom.ClinicRoomResponse;
import org.example.petcarebe.service.ClinicRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/clinic-rooms")
public class ClinicRoomController {

    @Autowired
    private ClinicRoomService clinicRoomService;

    @PostMapping
    public ResponseEntity<ClinicRoomResponse> createClinicRoom(@RequestBody CreateClinicRoomRequest request) {
        ClinicRoomResponse createdClinicRoom = clinicRoomService.createClinicRoom(request);
        return ResponseEntity.ok(createdClinicRoom);
    }

    @GetMapping
    public ResponseEntity<List<ClinicRoomResponse>> getAllClinicRooms() {
        List<ClinicRoomResponse> clinicRooms = clinicRoomService.getAllClinicRooms();
        return ResponseEntity.ok(clinicRooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicRoomResponse> getClinicRoomById(@PathVariable Long id) {
        ClinicRoomResponse clinicRoom = clinicRoomService.getClinicRoomById(id);
        return ResponseEntity.ok(clinicRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicRoomResponse> updateClinicRoom(@PathVariable Long id, @RequestBody UpdateClinicRoomRequest request) {
        ClinicRoomResponse updatedClinicRoom = clinicRoomService.updateClinicRoom(id, request);
        return ResponseEntity.ok(updatedClinicRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinicRoom(@PathVariable Long id) {
        clinicRoomService.deleteClinicRoom(id);
        return ResponseEntity.noContent().build();
    }
}


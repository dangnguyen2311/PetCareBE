package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.vaccine.CreateVaccineRequest;
import org.example.petcarebe.dto.request.vaccine.UpdateVaccineRequest;
import org.example.petcarebe.dto.response.vaccinationrecord.VaccinationRecordResponse;
import org.example.petcarebe.dto.response.vaccine.VaccineResponse;
import org.example.petcarebe.enums.InventoryObjectType;
import org.example.petcarebe.model.*;
import org.example.petcarebe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VaccineService {

    @Autowired
    private VaccineRepository vaccineRepository;
    @Autowired
    private InventoryObjectRepository inventoryObjectRepository;
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;

    public VaccineResponse createVaccine(CreateVaccineRequest request) {
        InventoryObject inventoryObject = new InventoryObject();
        inventoryObject.setName(request.getName());
        inventoryObject.setDescription(request.getDescription());
        inventoryObject.setType(InventoryObjectType.VACCINE);
        InventoryObject savedInventoryObject = inventoryObjectRepository.save(inventoryObject);

        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setMinQuantity(0);
        inventoryItem.setQuantity(0);
        inventoryItem.setName(request.getName());
        inventoryItem.setUnit("UNIT");
        inventoryItem.setCreatedAt(LocalDateTime.now());
        inventoryItem.setUpdatedAt(LocalDateTime.now());
        inventoryItem.setInventoryObject(savedInventoryObject);
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);

        Vaccine vaccine = new Vaccine();
        vaccine.setName(request.getName());
        vaccine.setManufacturer(request.getManufacturer());
        vaccine.setDescription(request.getDescription());
        vaccine.setIsDeleted(false);
        vaccine.setInventoryObject(savedInventoryObject);

        Vaccine savedVaccine = vaccineRepository.save(vaccine);
        return convertToResponse(savedVaccine);
    }

    public List<VaccineResponse> getAllVaccines() {
        return vaccineRepository.findAllByIsDeleted(false).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public VaccineResponse getVaccineById(Long id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id: " + id));
        return convertToResponse(vaccine, "Get Vaccine Response");
    }

    public VaccineResponse updateVaccine(Long id, UpdateVaccineRequest request) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id: " + id));

        vaccine.setName(request.getName());
        vaccine.setManufacturer(request.getManufacturer());
        vaccine.setDescription(request.getDescription());

        Vaccine updatedVaccine = vaccineRepository.save(vaccine);
        return convertToResponse(updatedVaccine, "Vaccine updated successfully");
    }

    public void deleteVaccine(Long id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id: " + id));
        vaccine.setIsDeleted(true);
        vaccineRepository.save(vaccine);
    }
    public List<VaccinationRecordResponse> getAllVaccinesByPetId(Long petId) {
        Pet peyById = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));
        List<VaccinationRecord> vaccinationRecordListByPet = vaccinationRecordRepository.findAllByPet(peyById);
        return vaccinationRecordListByPet.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private VaccineResponse convertToResponse(Vaccine vaccine) {
        return new VaccineResponse(
                vaccine.getId(),
                vaccine.getName(),
                vaccine.getManufacturer(),
                vaccine.getDescription(),
                vaccine.getInventoryObject() != null ? vaccine.getInventoryObject().getId() : null,
                vaccine.getInventoryObject() != null ? vaccine.getInventoryObject().getType() : null,
                ""
        );
    }
    private VaccineResponse convertToResponse(Vaccine vaccine, String message) {
        return new VaccineResponse(
                vaccine.getId(),
                vaccine.getName(),
                vaccine.getManufacturer(),
                vaccine.getDescription(),
                vaccine.getInventoryObject() != null ? vaccine.getInventoryObject().getId() : null,
                vaccine.getInventoryObject() != null ? vaccine.getInventoryObject().getType() : null,
                message
        );
    }
    private VaccinationRecordResponse convertToResponse(VaccinationRecord vaccinationRecord) {
        return VaccinationRecordResponse.builder()
                .vaccinationRecordId(vaccinationRecord.getId())
                .vaccinationDate(vaccinationRecord.getVaccinationDate())
                .nextDueDate(vaccinationRecord.getNextDueDate())
                .status(vaccinationRecord.getStatus())
                .notes(vaccinationRecord.getNotes())
                .doctorName(vaccinationRecord.getDoctor() != null ? vaccinationRecord.getDoctor().getFullname() : null)
                .vaccineName(vaccinationRecord.getVaccine() != null ? vaccinationRecord.getVaccine().getName() : null)
                .vaccineManufacture(vaccinationRecord.getVaccine() != null ? vaccinationRecord.getVaccine().getManufacturer() : null)
                .vaccineDescription(vaccinationRecord.getVaccine() != null ? vaccinationRecord.getVaccine().getDescription() : null)
                .message("")
                .build();

    }
    private VaccinationRecordResponse convertToResponse(VaccinationRecord vaccinationRecord, String message) {
        return VaccinationRecordResponse.builder()
                .vaccinationRecordId(vaccinationRecord.getId())
                .vaccinationDate(vaccinationRecord.getVaccinationDate())
                .nextDueDate(vaccinationRecord.getNextDueDate())
                .status(vaccinationRecord.getStatus())
                .notes(vaccinationRecord.getNotes())
                .doctorName(vaccinationRecord.getDoctor() != null ? vaccinationRecord.getDoctor().getFullname() : null)
                .vaccineName(vaccinationRecord.getVaccine() != null ? vaccinationRecord.getVaccine().getName() : null)
                .vaccineManufacture(vaccinationRecord.getVaccine() != null ? vaccinationRecord.getVaccine().getManufacturer() : null)
                .vaccineDescription(vaccinationRecord.getVaccine() != null ? vaccinationRecord.getVaccine().getDescription() : null)
                .message(message)
                .build();

    }
//    private LocalDate vaccinationDate;
//    private LocalDate nextDueDate;
//    private String status;
//    private String notes;
//    private String doctorName;
//    private String vaccineName;
//    private String vaccineManufacture;
//    private String vaccineDescription;
//    private String message;

}


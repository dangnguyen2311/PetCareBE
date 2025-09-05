package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.medicalrecord.UpdateMedicalRecordRequest;
import org.example.petcarebe.dto.response.medicalrecord.MedicalRecordResponse;
import org.example.petcarebe.model.MedicalRecord;
import org.example.petcarebe.model.Pet;
import org.example.petcarebe.repository.MedicalRecordRepository;
import org.example.petcarebe.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PetRepository petRepository;

    public List<MedicalRecordResponse> getMedicalRecords() {
        return medicalRecordRepository.findAll().stream()
                .map(this::convertToMedicalRecordResponse)
                .collect(Collectors.toList());
    }

    public MedicalRecordResponse updateMedicalRecord(Long petId, UpdateMedicalRecordRequest request) {
        Optional<Pet> petOptional = petRepository.findById(petId);
        if(petOptional.isEmpty()) {
            throw new RuntimeException("Pet not found");
        }
        Optional<MedicalRecord> medicalRecordOptional = medicalRecordRepository.findByPet(petOptional.get());
        if(medicalRecordOptional.isEmpty()) {
            throw new RuntimeException("Medical Record not found");
        }
        MedicalRecord medicalRecord = medicalRecordOptional.get();
        medicalRecord.setSummary(request.getSummary());
        MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medicalRecord);
        return convertToMedicalRecordResponse(savedMedicalRecord, "Medical Record successfully updated");

    }

    private MedicalRecordResponse convertToMedicalRecordResponse(MedicalRecord medicalRecord, String message) {
        return new MedicalRecordResponse(
                medicalRecord.getId(),
                medicalRecord.getCreatedDate(),
                medicalRecord.getUpdatedDate(),
                medicalRecord.getSummary(),
                message,
                medicalRecord.getPet().getId()
        );
    }

    private MedicalRecordResponse convertToMedicalRecordResponse(MedicalRecord medicalRecord) {
        return new MedicalRecordResponse(
                medicalRecord.getId(),
                medicalRecord.getCreatedDate(),
                medicalRecord.getUpdatedDate(),
                medicalRecord.getSummary(),
                "",
                medicalRecord.getPet().getId()

        );
    }


}


package org.example.petcarebe.controller;

import org.example.petcarebe.dto.request.medicalrecord.UpdateMedicalRecordRequest;
import org.example.petcarebe.dto.response.medicalrecord.MedicalRecordResponse;
import org.example.petcarebe.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/medical-records")
public class MedicalRecordController {
    // Your controller methods will go here

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping
    public ResponseEntity<List<MedicalRecordResponse>> getAllMedicalRecords() {
        List<MedicalRecordResponse> medicalRecordResponseList = medicalRecordService.getMedicalRecords();
        return ResponseEntity.ok().body(medicalRecordResponseList);
    }

    @PutMapping("/{petId}/update-medicalrecord")
    public ResponseEntity<MedicalRecordResponse> updateMedicalRecord(
            @PathVariable("petId") Long petId,
            @RequestBody UpdateMedicalRecordRequest request
    ) {
        MedicalRecordResponse updateMedicalRecord = medicalRecordService.updateMedicalRecord(petId, request);

        return ResponseEntity.ok().body(updateMedicalRecord);
    }
}


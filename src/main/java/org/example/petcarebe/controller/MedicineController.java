package org.example.petcarebe.controller;

import org.example.petcarebe.dto.request.medicine.CreateMedicineRequest;
import org.example.petcarebe.dto.request.medicine.UpdateMedicineRequest;
import org.example.petcarebe.dto.response.medicine.MedicineResponse;
import org.example.petcarebe.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @PostMapping
    public ResponseEntity<MedicineResponse> createMedicine(@RequestBody CreateMedicineRequest request) {
        MedicineResponse createdMedicine = medicineService.createMedicine(request);
        return ResponseEntity.ok(createdMedicine);
    }

    @GetMapping
    public ResponseEntity<List<MedicineResponse>> getAllMedicines() {
        List<MedicineResponse> medicines = medicineService.getAllMedicines();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> getMedicineById(@PathVariable Long id) {
        MedicineResponse medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(medicine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponse> updateMedicine(@PathVariable Long id, @RequestBody UpdateMedicineRequest request) {
        MedicineResponse updatedMedicine = medicineService.updateMedicine(id, request);
        return ResponseEntity.ok(updatedMedicine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }

}


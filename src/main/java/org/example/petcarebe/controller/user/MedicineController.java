package org.example.petcarebe.controller.user;

import org.example.petcarebe.dto.request.medicine.CreateMedicineRequest;
import org.example.petcarebe.dto.request.medicine.UpdateMedicineRequest;
import org.example.petcarebe.dto.response.medicine.MedicineResponse;
import org.example.petcarebe.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user/v1/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @PostMapping
    public ResponseEntity<MedicineResponse> createMedicine(@RequestBody CreateMedicineRequest request) {
        try {
            MedicineResponse createdMedicine = medicineService.createMedicine(request);
            return ResponseEntity.ok(createdMedicine);
        } catch (RuntimeException e) {
            MedicineResponse error = new MedicineResponse();
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            MedicineResponse error = new MedicineResponse();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping
    public ResponseEntity<List<MedicineResponse>> getAllMedicines() {
        try {
            List<MedicineResponse> medicines = medicineService.getAllMedicines();
            return ResponseEntity.ok(medicines);
        } catch (RuntimeException e) {
            List<MedicineResponse> error = new ArrayList<>();
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            List<MedicineResponse> error = new ArrayList<>();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> getMedicineById(@PathVariable Long id) {
        try {
            MedicineResponse medicine = medicineService.getMedicineById(id);
            return ResponseEntity.ok(medicine);
        } catch (RuntimeException e) {
            MedicineResponse error = new MedicineResponse();
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            MedicineResponse error = new MedicineResponse();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponse> updateMedicine(@PathVariable Long id, @RequestBody UpdateMedicineRequest request) {
        try {
            MedicineResponse updatedMedicine = medicineService.updateMedicine(id, request);
            return ResponseEntity.ok(updatedMedicine);
        } catch (RuntimeException e) {
            MedicineResponse error = new MedicineResponse();
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            MedicineResponse error = new MedicineResponse();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        try {
            medicineService.deleteMedicine(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e) {
            return  ResponseEntity.internalServerError().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}


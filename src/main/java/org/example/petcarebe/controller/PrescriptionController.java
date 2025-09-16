package org.example.petcarebe.controller;

import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.prescription.CreatePrescriptionRequest;
import org.example.petcarebe.dto.request.prescription.UpdatePrescriptionInvoiceRequest;
import org.example.petcarebe.dto.response.prescription.PrescriptionResponse;
import org.example.petcarebe.model.Prescription;
import org.example.petcarebe.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/admin/v1/prescription")
public class PrescriptionController {
    @Autowired
    private PrescriptionService  prescriptionService;

    @PostMapping
    public ResponseEntity<PrescriptionResponse> createPrescription(@Valid @RequestBody CreatePrescriptionRequest request) {
        try{
            PrescriptionResponse response = prescriptionService.createPrescription(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch(RuntimeException e){
            // Log the actual error for debugging
            System.err.println("Error creating prescription: " + e.getMessage());
            e.printStackTrace();

            PrescriptionResponse errorResponse = new PrescriptionResponse();
            errorResponse.setNote("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        catch(Exception e){
            // Log unexpected errors
            System.err.println("Unexpected error creating prescription: " + e.getMessage());
            e.printStackTrace();

            PrescriptionResponse errorResponse = new PrescriptionResponse();
            errorResponse.setNote("Unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get prescription by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> getPrescriptionById(@PathVariable Long id) {
        try {
            PrescriptionResponse response = prescriptionService.getPrescriptionById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error getting prescription: " + e.getMessage());
            PrescriptionResponse errorResponse = new PrescriptionResponse();
            errorResponse.setNote("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Update prescription with invoice
     */
    @PutMapping("/{prescriptionId}/invoice")
    public ResponseEntity<PrescriptionResponse> updatePrescriptionInvoice(
            @PathVariable Long prescriptionId,
            @Valid @RequestBody UpdatePrescriptionInvoiceRequest request) {
        try {
            PrescriptionResponse response = prescriptionService.updatePrescriptionInvoice(prescriptionId, request.getInvoiceId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error updating prescription invoice: " + e.getMessage());
            PrescriptionResponse errorResponse = new PrescriptionResponse();
            errorResponse.setNote("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}

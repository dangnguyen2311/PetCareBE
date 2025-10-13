package org.example.petcarebe.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.petcarebe.dto.request.prescription.*;
import org.example.petcarebe.dto.response.doctor.DoctorResponse;
import org.example.petcarebe.dto.response.prescription.PrescriptionItemResponse;
import org.example.petcarebe.dto.response.prescription.PrescriptionResponse;
import org.example.petcarebe.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.rmi.server.LogStream.log;

@Slf4j
@RestController
@RequestMapping("/api/admin/v1/prescription")
@Tag(name = "💊 Prescription Management", description = "Admin endpoints for managing prescriptions and prescription items")
public class PrescriptionController {
    @Autowired
    private PrescriptionService  prescriptionService;

    @Operation(
            summary = "Create new prescription",
            description = "Create a new prescription with optional invoice association"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prescription created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<PrescriptionResponse> createPrescription(
            @Valid @RequestBody CreatePrescriptionWithoutInvoiceRequest request) {
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

    @PostMapping("/create-with-invoice")
    public ResponseEntity<PrescriptionResponse> createPrescriptionWithInvoice(
            @Valid @RequestBody CreatePrescriptionRequest request) {
        try{
            PrescriptionResponse response = prescriptionService.createPrescriptionWithInvoice(request);
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


    @Operation(
            summary = "Get prescription by ID",
            description = "Retrieve a prescription with all its items by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Prescription not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> getPrescriptionById(
            @Parameter(description = "ID of the prescription", example = "1", required = true)
            @PathVariable Long id) {
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


    @Operation(
            summary = "Update prescription with invoice",
            description = "Associate a prescription with an invoice"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Prescription or invoice not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{prescriptionId}/invoice")
    public ResponseEntity<PrescriptionResponse> updatePrescriptionInvoice(
            @Parameter(description = "ID of the prescription", example = "1", required = true)
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

    // ==================== PRESCRIPTION ITEM ENDPOINTS ====================

    @Operation(
            summary = "Add prescription item",
            description = "Add a new prescription item (medicine) to an existing prescription"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prescription item added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Prescription or medicine not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{prescriptionId}/items")
    public ResponseEntity<PrescriptionItemResponse> addPrescriptionItem(
            @Parameter(description = "ID of the prescription", example = "1", required = true)
            @PathVariable Long prescriptionId,
            @Valid @RequestBody CreatePrescriptionItemRequest request) {
        try {
            System.out.println("Add prescription item");
            PrescriptionItemResponse response = prescriptionService.addPrescriptionItem(prescriptionId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            PrescriptionItemResponse errorResponse = new PrescriptionItemResponse();
            errorResponse.setMessage("Error of PrescriptionItemResponse: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            PrescriptionItemResponse errorResponse = new PrescriptionItemResponse();
            errorResponse.setMessage("Error of PrescriptionItemResponse: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(
            summary = "Get prescription items",
            description = "Get all prescription items for a specific prescription"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription items retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Prescription not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{prescriptionId}/items")
    public ResponseEntity<List<PrescriptionItemResponse>> getPrescriptionItems(
            @Parameter(description = "ID of the prescription", example = "1", required = true)
            @PathVariable Long prescriptionId) {
        try {
            List<PrescriptionItemResponse> items = prescriptionService.getPrescriptionItems(prescriptionId);
            return ResponseEntity.ok(items);
        } catch (RuntimeException e) {
            List<PrescriptionItemResponse> errorResponse = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            List<PrescriptionItemResponse> errorResponse = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(
            summary = "Get prescription item by ID",
            description = "Get a specific prescription item by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Prescription item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/items/{itemId}")
    public ResponseEntity<PrescriptionItemResponse> getPrescriptionItemById(
            @Parameter(description = "ID of the prescription item", example = "1", required = true)
            @PathVariable Long itemId) {
        try {
            PrescriptionItemResponse item = prescriptionService.getPrescriptionItemById(itemId);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            PrescriptionItemResponse errorResponse = new PrescriptionItemResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            PrescriptionItemResponse errorResponse = new PrescriptionItemResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(
            summary = "Update prescription item",
            description = "Update an existing prescription item"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Prescription item or medicine not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/items/{itemId}")
    public ResponseEntity<PrescriptionItemResponse> updatePrescriptionItem(
            @Parameter(description = "ID of the prescription item", example = "1", required = true)
            @PathVariable Long itemId,
            @Valid @RequestBody UpdatePrescriptionItemRequest request) {
        try {
            PrescriptionItemResponse response = prescriptionService.updatePrescriptionItem(itemId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            PrescriptionItemResponse errorResponse = new PrescriptionItemResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            PrescriptionItemResponse errorResponse = new PrescriptionItemResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(
            summary = "Delete prescription item",
            description = "Delete a prescription item from a prescription"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prescription item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Prescription item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deletePrescriptionItem(
            @Parameter(description = "ID of the prescription item", example = "1", required = true)
            @PathVariable Long itemId) {
        try {
            prescriptionService.deletePrescriptionItem(itemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            PrescriptionItemResponse errorResponse = new PrescriptionItemResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(
            summary = "Get all prescriptions",
            description = "Get all prescriptions in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescriptions retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<PrescriptionResponse>> getAllPrescriptions() {
        try {
            List<PrescriptionResponse> prescriptions = prescriptionService.getAllPrescriptions();
            return ResponseEntity.ok(prescriptions);
        }
        catch (RuntimeException e) {
            List<PrescriptionResponse> error = new ArrayList<>();
            System.err.println("Error getting all prescriptions: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
        catch (Exception e) {
            List<PrescriptionResponse> error = new ArrayList<>();
            System.err.println("Error getting all prescriptions: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PrescriptionResponse>> getAllPrescriptionsByDoctor(
            @PathVariable Long doctorId,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        try{
            List<PrescriptionResponse> responses = prescriptionService.getAllPrescriptionsByDoctor(doctorId, date);
            return ResponseEntity.ok(responses);
        } catch (RuntimeException e) {
            List<PrescriptionResponse> error = new ArrayList<>();
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            List<PrescriptionResponse> error = new ArrayList<>();
            System.out.println(e.getMessage());
            return  ResponseEntity.badRequest().body(error);
        }
    }

    @Operation(
            summary = "Delete prescription",
            description = "Delete a prescription and all its items"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prescription deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Prescription not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(
            @Parameter(description = "ID of the prescription", example = "1", required = true)
            @PathVariable Long id) {
        try {
            prescriptionService.deletePrescription(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.err.println("Error deleting prescription: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        catch (Exception e) {
            System.err.println("Error deleting prescription: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}

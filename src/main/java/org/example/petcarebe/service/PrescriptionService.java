package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.prescription.CreatePrescriptionRequest;
import org.example.petcarebe.dto.response.prescription.PrescriptionResponse;
import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.Prescription;
import org.example.petcarebe.repository.InvoiceRepository;
import org.example.petcarebe.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public PrescriptionResponse createPrescription(CreatePrescriptionRequest request) {
        Invoice invoice = null;
        Long invoiceId = null;

        // Only validate and fetch invoice if invoiceId is provided
        if (request.getInvoiceId() != null) {
            invoice = invoiceRepository.findById(request.getInvoiceId())
                    .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + request.getInvoiceId()));
            invoiceId = invoice.getId();
        }

        Prescription prescription = new Prescription();
        prescription.setInvoice(invoice); // Can be null
        prescription.setCreatedDate(LocalDate.now());
        prescription.setNotes(request.getNotes());

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return convertToResponse(savedPrescription, invoiceId);
    }
    /**
     * Update prescription with invoice
     */
    public PrescriptionResponse updatePrescriptionInvoice(Long prescriptionId, Long invoiceId) {
        // Validate prescription exists
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + prescriptionId));

        // Validate invoice exists
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));

        // Update prescription with invoice
        prescription.setInvoice(invoice);
        Prescription updatedPrescription = prescriptionRepository.save(prescription);

        return convertToResponse(updatedPrescription, invoiceId);
    }

    /**
     * Get prescription by ID
     */
    public PrescriptionResponse getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));

        Long invoiceId = null;
        if (prescription.getInvoice() != null) {
            invoiceId = prescription.getInvoice().getId();
        }

        return convertToResponse(prescription, invoiceId);
    }

    private PrescriptionResponse convertToResponse(Prescription prescription, Long invoiceId) {
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .note(prescription.getNotes())
                .createdDate(prescription.getCreatedDate())
                .invoiceId(invoiceId) // Use passed invoiceId to avoid lazy loading
                .build();
    }
}

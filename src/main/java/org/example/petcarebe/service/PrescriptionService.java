package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.prescription.CreatePrescriptionItemRequest;
import org.example.petcarebe.dto.request.prescription.CreatePrescriptionRequest;
import org.example.petcarebe.dto.request.prescription.UpdatePrescriptionItemRequest;
import org.example.petcarebe.dto.response.prescription.PrescriptionItemResponse;
import org.example.petcarebe.dto.response.prescription.PrescriptionResponse;
import org.example.petcarebe.enums.StockMovementType;
import org.example.petcarebe.model.*;
import org.example.petcarebe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PrescriptionItemRepository prescriptionItemRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private InventoryObjectRepository  inventoryObjectRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    @Autowired
    private MedicinePriceHistoryRepository medicinePriceHistoryRepository;
    @Autowired
    private StockMovementRepository stockMovementRepository;
    @Autowired
    private StockMovement_PrescriptionItemRepository stockMovement_PrescriptionItemRepository;

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

    // ==================== PRESCRIPTION ITEM METHODS ====================

    /**
     * Add prescription item to prescription
     */
    @Transactional
    public PrescriptionItemResponse addPrescriptionItem(Long prescriptionId, CreatePrescriptionItemRequest request) {
        // Validate prescription exists
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + prescriptionId));

        // Validate medicine exists
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + request.getMedicineId()));

        MedicinePriceHistory medicinePriceHistory = medicinePriceHistoryRepository.findByStatus("ACTIVE")
                .orElseThrow(() -> new RuntimeException("Medicine price history not found with id: " + request.getMedicineId()));

        // Create prescription item

        InventoryObject inventoryObjectByMedicine = medicine.getInventoryObject();
        InventoryItem inventoryItemByMedicine = inventoryItemRepository.findByInventoryObject(inventoryObjectByMedicine)
                .orElseThrow(() -> new RuntimeException("Inventory item not found with id: " + inventoryObjectByMedicine.getId()));
        inventoryItemByMedicine.setQuantity(adjustQuantity(StockMovementType.SALE, request.getQuantity(), inventoryItemByMedicine.getQuantity()));
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItemByMedicine);
        // tạo stockmovement
        StockMovement stockMovement = new StockMovement();
        stockMovement.setQuantity(request.getQuantity());
        stockMovement.setMovementType(StockMovementType.SALE);
        stockMovement.setCreatedAt(LocalDate.now());
        stockMovement.setPrice(medicinePriceHistory.getPrice());
        stockMovement.setMovementDate(LocalDateTime.now());
        stockMovement.setInventoryItem(savedInventoryItem);

        StockMovement savedStockMovement = stockMovementRepository.save(stockMovement);

        PrescriptionItem prescriptionItem = new PrescriptionItem();
        prescriptionItem.setPrescription(prescription);
        prescriptionItem.setMedicine(medicine);
        prescriptionItem.setQuantity(request.getQuantity());
        prescriptionItem.setDosage(request.getDosage());
        prescriptionItem.setDuration(request.getDuration());
        prescriptionItem.setInstruction(request.getInstruction());
        prescriptionItem.setPrice(request.getPrice());
        prescriptionItem.setTaxPercent(request.getTaxPercent());
        prescriptionItem.setPromotionAmount(request.getPromotionAmount());

        PrescriptionItem savedItem = prescriptionItemRepository.save(prescriptionItem);

        StockMovement_PrescriptionItem stockMovementPrescriptionItem = new StockMovement_PrescriptionItem();
        stockMovementPrescriptionItem.setPrescriptionItem(prescriptionItem);
        stockMovementPrescriptionItem.setStockMovement(savedStockMovement);
        StockMovement_PrescriptionItem savedStockMovementPrescriptionItem = stockMovement_PrescriptionItemRepository.save(stockMovementPrescriptionItem);

        return convertToItemResponse(savedItem);
    }

    /**
     * Get all prescription items for a prescription
     */
    public List<PrescriptionItemResponse> getPrescriptionItems(Long prescriptionId) {
        // Validate prescription exists
        prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + prescriptionId));

        List<PrescriptionItem> items = prescriptionItemRepository.findByPrescriptionId(prescriptionId);
        return items.stream()
                .map(this::convertToItemResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get prescription item by ID
     */
    public PrescriptionItemResponse getPrescriptionItemById(Long itemId) {
        PrescriptionItem item = prescriptionItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Prescription item not found with id: " + itemId));
        return convertToItemResponse(item);
    }

    /**
     * Update prescription item
     */
    @Transactional
    public PrescriptionItemResponse updatePrescriptionItem(Long itemId, UpdatePrescriptionItemRequest request) {
        // Validate prescription item exists
        PrescriptionItem item = prescriptionItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Prescription item not found with id: " + itemId));

        // Validate medicine exists
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + request.getMedicineId()));

        // Update prescription item
        item.setMedicine(medicine);
        item.setDosage(request.getDosage());
        item.setDuration(request.getDuration());
        item.setInstruction(request.getInstruction());
        item.setPrice(request.getPrice());
        item.setTaxPercent(request.getTaxPercent());
        item.setPromotionAmount(request.getPromotionAmount());

        PrescriptionItem updatedItem = prescriptionItemRepository.save(item);
        return convertToItemResponse(updatedItem);
    }

    /**
     * Delete prescription item
     */
    @Transactional
    public void deletePrescriptionItem(Long itemId) {
        PrescriptionItem item = prescriptionItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Prescription item not found with id: " + itemId));
        prescriptionItemRepository.delete(item);
    }

    /**
     * Get all prescriptions
     */
    public List<PrescriptionResponse> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        return prescriptions.stream()
                .map(prescription -> {
                    Long invoiceId = prescription.getInvoice() != null ? prescription.getInvoice().getId() : null;
                    return convertToResponse(prescription, invoiceId);
                })
                .collect(Collectors.toList());
    }
    private Integer adjustQuantity(StockMovementType type, Integer requestQuantity, Integer inventoryItemQuantity) {
        return switch (type) {
            case IN, PURCHASE, RETURN, ADJUSTMENT ->
                    inventoryItemQuantity + requestQuantity;

            case OUT, SALE, EXPIRED, LOST, PRESCRIPTION, TREATMENT -> {
                if (inventoryItemQuantity - requestQuantity >= 0) {
                    yield inventoryItemQuantity - requestQuantity;
                } else {
                    throw new RuntimeException("Not enough stock for movement: " + type);
                }
            }

            case TRANSFER -> inventoryItemQuantity;

            default -> throw new RuntimeException("Invalid Movement Type");
        };
    }

    /**
     * Delete prescription and all its items
     */
    @Transactional
    public void deletePrescription(Long prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + prescriptionId));

        // Delete all prescription items first
        prescriptionItemRepository.deleteByPrescriptionId(prescriptionId);

        // Then delete the prescription
        prescriptionRepository.delete(prescription);
    }

    // ==================== CONVERTER METHODS ====================

    private PrescriptionResponse convertToResponse(Prescription prescription, Long invoiceId) {
        // Get prescription items
        List<PrescriptionItem> items = prescriptionItemRepository.findByPrescriptionId(prescription.getId());
        List<PrescriptionItemResponse> itemResponses = items.stream()
                .map(this::convertToItemResponse)
                .collect(Collectors.toList());

        // Calculate totals
        Double totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() + (item.getPrice() * item.getTaxPercent() / 100) - item.getPromotionAmount())
                .sum();

        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .note(prescription.getNotes())
                .createdDate(prescription.getCreatedDate())
                .invoiceId(invoiceId)
                .items(itemResponses)
                .totalAmount(totalAmount)
                .itemCount(items.size())
                .build();
    }

    private PrescriptionItemResponse convertToItemResponse(PrescriptionItem item) {
        Double totalAmount = item.getPrice() + (item.getPrice() * item.getTaxPercent() / 100) - item.getPromotionAmount();

        return PrescriptionItemResponse.builder()
                .id(item.getId())
                .dosage(item.getDosage())
                .duration(item.getDuration())
                .quantity(item.getQuantity())
                .instruction(item.getInstruction())
                .price(item.getPrice())
                .taxPercent(item.getTaxPercent())
                .promotionAmount(item.getPromotionAmount())
                .totalAmount(totalAmount)
                .medicineId(item.getMedicine().getId())
                .medicineName(item.getMedicine().getName())
                .medicineDescription(item.getMedicine().getDescription())
                .prescriptionId(item.getPrescription().getId())
                .build();
    }
}

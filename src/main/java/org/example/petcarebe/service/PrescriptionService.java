package org.example.petcarebe.service;

import jakarta.validation.Valid;
import org.example.petcarebe.dto.request.prescription.CreatePrescriptionItemRequest;
import org.example.petcarebe.dto.request.prescription.CreatePrescriptionRequest;
import org.example.petcarebe.dto.request.prescription.CreatePrescriptionWithoutInvoiceRequest;
import org.example.petcarebe.dto.request.prescription.UpdatePrescriptionItemRequest;
import org.example.petcarebe.dto.response.prescription.PrescriptionItemResponse;
import org.example.petcarebe.dto.response.prescription.PrescriptionResponse;
import org.example.petcarebe.enums.StockMovementType;
import org.example.petcarebe.model.*;
import org.example.petcarebe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    private DoctorRepository doctorRepository;

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
    @Autowired
    private MedicineInPromotionRepository  medicineInPromotionRepository;

    public PrescriptionResponse createPrescription(@Valid CreatePrescriptionWithoutInvoiceRequest request) {
        Doctor doctorById = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor Not Found"));
        Prescription prescription = new Prescription();
        prescription.setCreatedDate(LocalDate.now());
        prescription.setNotes(request.getNotes());
        prescription.setDoctor(doctorById);

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return convertToResponse(savedPrescription);
    }

    public PrescriptionResponse createPrescriptionWithInvoice(CreatePrescriptionRequest  request) {

        Doctor doctorById = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor Not Found"));
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElse(new Invoice());

        Prescription prescription = new Prescription();
        prescription.setInvoice(invoice); // Can be null
        prescription.setCreatedDate(LocalDate.now());
        prescription.setNotes(request.getNotes());
        prescription.setDoctor(doctorById);

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return convertToResponse(savedPrescription);
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

        return convertToResponse(updatedPrescription);
    }

    /**
     * Get prescription by ID
     */
    public PrescriptionResponse getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));
        return convertToResponse(prescription);
    }

    // ==================== PRESCRIPTION ITEM METHODS ====================

    /**
     * Add prescription item to prescription
     */
//    @Transactional
    public PrescriptionItemResponse addPrescriptionItem(
            Long prescriptionId,
            CreatePrescriptionItemRequest request) {
        System.out.println("Search Promotion for Medicine: " + 000.1);
        // Validate prescription exists
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + prescriptionId));

        // Validate medicine exists
        System.out.println("Search Promotion for Medicine: " + 000.2);
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + request.getMedicineId()));

        //Get price
        System.out.println("Search Promotion for Medicine: " + 000.3);
        MedicinePriceHistory medicinePriceHistory = medicinePriceHistoryRepository.findByStatusAndMedicine("ACTIVE", medicine)
                .orElseThrow(() -> new RuntimeException("Medicine price history not found with id: " + request.getMedicineId()));

        // Get Promotion for Medicine
        Double totalPromotionAmount = 0.0;
        System.out.println("Search Promotion for Medicine: " + 0.4);
        List<MedicineInPromotion> medicineInPromotionList = medicineInPromotionRepository.findAllByMedicine(medicine);
        totalPromotionAmount = getTotalPromotionAmount(medicineInPromotionList, medicinePriceHistory.getPrice());

        // Create prescription item
        System.out.println("Search InventoryItem: " + totalPromotionAmount);
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
        prescriptionItem.setPrice(medicinePriceHistory.getPrice());
        prescriptionItem.setTaxPercent(request.getTaxPercent());
        prescriptionItem.setPromotionAmount(totalPromotionAmount);
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
    public PrescriptionItemResponse updatePrescriptionItem(Long prescriptionItemId, UpdatePrescriptionItemRequest request) {
        // Validate prescription item exists
        PrescriptionItem prescriptionItem = prescriptionItemRepository.findById(prescriptionItemId)
                .orElseThrow(() -> new RuntimeException("Prescription item not found with id: " + prescriptionItemId));
        int beforeQuantity = prescriptionItem.getQuantity();

        // Validate medicine exists
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + request.getMedicineId()));

        //Get price
        MedicinePriceHistory medicinePriceHistory = medicinePriceHistoryRepository.findByStatusAndMedicine("ACTIVE", medicine)
                .orElseThrow(() -> new RuntimeException("Medicine price history not found with id: " + request.getMedicineId()));

        // Get Promotion for Medicine
        Double totalPromotionAmount = 0.0;
        List<MedicineInPromotion> medicineInPromotionList = medicineInPromotionRepository.findAllByMedicine(medicine);
        totalPromotionAmount = getTotalPromotionAmount(medicineInPromotionList, medicinePriceHistory.getPrice());

        // Get StockMovement_PrescriptionItem
        StockMovement_PrescriptionItem  stockMovementPrescriptionItemToUpdate =
                stockMovement_PrescriptionItemRepository.findTopByPrescriptionItemOrderByCreatedAtDesc(prescriptionItem)
                        .orElseThrow(() -> new RuntimeException("StockMovement_PrescriptionItem not found"));
        // Lỗi ở chỗ StockMovement_PrescriptionItem có thể có nhiều kqua, nếu chỉ sửa 1 lần thì okee
        StockMovement stockMovementToUpdate = stockMovementPrescriptionItemToUpdate.getStockMovement();


        // Update prescription item
        InventoryObject inventoryObjectByMedicine = medicine.getInventoryObject();
        InventoryItem inventoryItemByMedicine = inventoryItemRepository.findByInventoryObject(inventoryObjectByMedicine)
                .orElseThrow(() -> new RuntimeException("Inventory item not found with id: " + inventoryObjectByMedicine.getId()));

        inventoryItemByMedicine.setQuantity(adjustQuantity(StockMovementType.ADJUSTMENT,
                request.getQuantity() - beforeQuantity,
                inventoryItemByMedicine.getQuantity()));
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItemByMedicine);

        // tạo stockmovement, xử lý thêm bớt
        StockMovement stockMovement = new StockMovement();
        stockMovement.setQuantity(request.getQuantity() - beforeQuantity);
        stockMovement.setMovementType(StockMovementType.ADJUSTMENT);
        stockMovement.setCreatedAt(LocalDate.now());
        stockMovement.setPrice(medicinePriceHistory.getPrice());
        stockMovement.setMovementDate(LocalDateTime.now());
        stockMovement.setInventoryItem(savedInventoryItem);
        StockMovement savedStockMovement = stockMovementRepository.save(stockMovement);

        // Update prescription item
        prescriptionItem.setMedicine(medicine);
        prescriptionItem.setDosage(request.getDosage());
        prescriptionItem.setDuration(request.getDuration());
        prescriptionItem.setInstruction(request.getInstruction());
        prescriptionItem.setPrice(medicinePriceHistory.getPrice());
        prescriptionItem.setTaxPercent(request.getTaxPercent());
        prescriptionItem.setPromotionAmount(totalPromotionAmount);
        prescriptionItem.setQuantity(request.getQuantity());

        PrescriptionItem updatedItem = prescriptionItemRepository.save(prescriptionItem);

        // Save new StockMovement for PrescriptionItem
        StockMovement_PrescriptionItem stockMovementPrescriptionItem = new StockMovement_PrescriptionItem();
        stockMovementPrescriptionItem.setPrescriptionItem(updatedItem);
        stockMovementPrescriptionItem.setStockMovement(savedStockMovement);
        stockMovementPrescriptionItem.setCreatedAt(LocalDateTime.now());
        StockMovement_PrescriptionItem savedStockMovementPrescriptionItem = stockMovement_PrescriptionItemRepository.save(stockMovementPrescriptionItem);

        return convertToItemResponse(updatedItem);
    }

    /**
     * Delete prescription item
     */
    @Transactional
    public void deletePrescriptionItem(Long itemId) {
        PrescriptionItem item = prescriptionItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Prescription item not found with id: " + itemId));
        //Delete from Stock
        List<StockMovement_PrescriptionItem> stockMovementPrescriptionItem = stockMovement_PrescriptionItemRepository.findAllByPrescriptionItem(item);
        for (StockMovement_PrescriptionItem i : stockMovementPrescriptionItem) {
            StockMovement stockMovement = i.getStockMovement();
            stockMovement_PrescriptionItemRepository.delete(i);
            stockMovementRepository.delete(stockMovement);
        }

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
                    return convertToResponse(prescription);
                })
                .collect(Collectors.toList());
    }
    private Integer adjustQuantity(StockMovementType type, Integer requestQuantity, Integer inventoryItemQuantity) {
        //requestQuantity = before - after
        return switch (type) {
            case IN, PURCHASE, RETURN, ADJUSTMENT ->
                    inventoryItemQuantity - requestQuantity;

            case OUT, SALE, EXPIRED, LOST, PRESCRIPTION, TREATMENT -> {
                if (inventoryItemQuantity - requestQuantity >= 0) {
                    yield inventoryItemQuantity - requestQuantity;
                } else {
                    System.out.println("Not enough inventory items for ");
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

    public List<PrescriptionResponse> getAllPrescriptionsByDoctor(Long doctorId, LocalDate date) {
        if(date == null) date = LocalDate.now();
        Doctor doctorById = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        List<Prescription> prescriptionList = prescriptionRepository.
                findAllByDoctorAndCreatedDate(doctorById, date, Sort.by(Sort.Order.desc("createdDate")));
        return prescriptionList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ==================== CONVERTER METHODS ====================

    private Double getTotalPromotionAmount(List<MedicineInPromotion> medicineInPromotions, Double price) {
        Double totalPromotionAmount = 0.0;
        for (MedicineInPromotion item : medicineInPromotions) {
            if(!item.getPromotion().getIsDeleted()){
                if(item.getPromotion().getType().equals("PERCENT")){
                    totalPromotionAmount += (item.getPromotion().getValue() * price);
                }
                else if(item.getPromotion().getType().equals("CASH")){
                    totalPromotionAmount += item.getPromotion().getValue();
                }
            }
        }
        return totalPromotionAmount;
    }

    private PrescriptionResponse convertToResponse(Prescription prescription) {
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
                .invoiceId(prescription.getInvoice() != null ? prescription.getInvoice().getId() : null)
                .items(itemResponses)
                .doctorId(prescription.getDoctor() != null ? prescription.getDoctor().getId() : null)
                .doctorName(prescription.getDoctor() != null ? prescription.getDoctor().getFullname() : null)
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

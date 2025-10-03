package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.stockmovement.CreateStockMovementPrescriptionItemRequest;
import org.example.petcarebe.dto.request.stockmovement.CreateStockMovementProductRequest;
import org.example.petcarebe.dto.request.stockmovement.CreateStockMovementRequest;
import org.example.petcarebe.dto.request.stockmovement.CreateStockMovementVaccineRequest;
import org.example.petcarebe.dto.response.stockmovement.*;
import org.example.petcarebe.enums.StockMovementType;
import org.example.petcarebe.model.*;
import org.example.petcarebe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class StockMovementService {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private PrescriptionItemRepository prescriptionItemRepository;

    @Autowired
    private StockMovement_PrescriptionItemRepository stockMovement_PrescriptionItemRepository;

    @Autowired
    private StockMovement_ProductInInvoiceRepository stockMovement_ProductInInvoiceRepository;

    @Autowired
    private StockMovement_VaccineInInvoiceRepository stockMovement_VaccineInInvoiceRepository;

    @Autowired
    private ProductInInvoiceRepository  productInInvoiceRepository;

    @Autowired
    private VaccineInInvoiceRepository vaccineInInvoiceRepository;


    public StockMovementResponse getStockMovementById(Long stockMovementId) {
        StockMovement stockMovement = stockMovementRepository.findById(stockMovementId).
                orElseThrow(() -> new RuntimeException("StockMovement Not Found"));
        return convertToStockMovementResponse(stockMovement);
    }

    public List<StockMovementResponse> getAllStockMovements() {
        List<StockMovement> stockMovements = stockMovementRepository.findAll();
        return stockMovements.stream()
                .map(this::convertToStockMovementResponse)
                .toList();
    }

    public CreateStockMovementPrescriptionItemResponse createStockMovementPrescriptionItem(CreateStockMovementPrescriptionItemRequest request) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(request.getInventoryItemId()).
                orElseThrow(() -> new RuntimeException("InventoryItem Not Found"));
        inventoryItem.setQuantity(adjustQuantity(request.getMovementType(), request.getQuantity(), inventoryItem.getQuantity()));
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);

        StockMovement stockMovement = new StockMovement();
        stockMovement.setInventoryItem(savedInventoryItem);
        stockMovement.setMovementType(request.getMovementType());
        stockMovement.setQuantity(request.getQuantity());
        stockMovement.setPrice(request.getPrice());
        stockMovement.setMovementDate(request.getMovementDate());
        stockMovement.setNotes(request.getNotes());
        stockMovement.setCreatedAt(LocalDate.now());

        StockMovement savedStockMovement = stockMovementRepository.save(stockMovement);

        PrescriptionItem  prescriptionItem = prescriptionItemRepository.findById(request.getPrescriptionItemId())
                .orElseThrow(() -> new RuntimeException("PrescriptionItem Not Found"));


        StockMovement_PrescriptionItem stockMovementPrescription = new StockMovement_PrescriptionItem();
        stockMovementPrescription.setStockMovement(savedStockMovement);
        stockMovementPrescription.setPrescriptionItem(prescriptionItem);

        StockMovement_PrescriptionItem savedStockMovementPrescriptionItem = stockMovement_PrescriptionItemRepository.save(stockMovementPrescription);

        return convertToStockMovementPrescriptionItemResponse(savedStockMovement, savedStockMovementPrescriptionItem);

    }
    public CreateStockMovementProductResponse createStockMovementProduct(CreateStockMovementProductRequest request) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(request.getInventoryItemId()).
                orElseThrow(() -> new RuntimeException("InventoryItem Not Found"));

        inventoryItem.setQuantity(adjustQuantity(request.getMovementType(), request.getQuantity(), inventoryItem.getQuantity()));
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);

        StockMovement stockMovement = new StockMovement();
        stockMovement.setInventoryItem(savedInventoryItem);
        stockMovement.setMovementType(request.getMovementType());
        stockMovement.setQuantity(request.getQuantity());
        stockMovement.setPrice(request.getPrice());
        stockMovement.setMovementDate(request.getMovementDate());
        stockMovement.setNotes(request.getNotes());
        stockMovement.setCreatedAt(LocalDate.now());

        StockMovement savedStockMovement = stockMovementRepository.save(stockMovement);

        ProductInInvoice  productInInvoice = productInInvoiceRepository.findById(request.getProductInInvoiceId())
                .orElseThrow(() -> new RuntimeException("ProductInInvoice Not Found"));


        StockMovement_ProductInInvoice stockMovementProductInInvoice= new StockMovement_ProductInInvoice();
        stockMovementProductInInvoice.setStockMovement(savedStockMovement);
        stockMovementProductInInvoice.setProductInInvoice(productInInvoice);

        StockMovement_ProductInInvoice savedStockMovementProductInInvoice = stockMovement_ProductInInvoiceRepository.save(stockMovementProductInInvoice);

        return convertToStockMovementProductInInvoiceResponse(savedStockMovement, savedStockMovementProductInInvoice);

    }
    public CreateStockMovementVaccineResponse createStockMovementVaccine(CreateStockMovementVaccineRequest request) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(request.getInventoryItemId()).
                orElseThrow(() -> new RuntimeException("InventoryItem Not Found"));

        inventoryItem.setQuantity(adjustQuantity(request.getMovementType(), request.getQuantity(), inventoryItem.getQuantity()));
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);

        StockMovement stockMovement = new StockMovement();
        stockMovement.setInventoryItem(savedInventoryItem);
        stockMovement.setMovementType(request.getMovementType());
        stockMovement.setQuantity(request.getQuantity());
        stockMovement.setPrice(request.getPrice());
        stockMovement.setMovementDate(request.getMovementDate());
        stockMovement.setNotes(request.getNotes());
        stockMovement.setCreatedAt(LocalDate.now());

        StockMovement savedStockMovement = stockMovementRepository.save(stockMovement);

        VaccineInInvoice  vaccineInInvoice = vaccineInInvoiceRepository.findById(request.getVaccineInInvoiceId())
                .orElseThrow(() -> new RuntimeException("VaccineInInvoice Not Found"));


        StockMovement_VaccineInInvoice stockMovementVaccineInInvoice= new StockMovement_VaccineInInvoice();
        stockMovementVaccineInInvoice.setStockMovement(savedStockMovement);
        stockMovementVaccineInInvoice.setVaccineInInvoice(vaccineInInvoice);

        StockMovement_VaccineInInvoice savedStockMovementVaccineInInvoice = stockMovement_VaccineInInvoiceRepository.save(stockMovementVaccineInInvoice);

        return convertToStockMovementVaccineInInvoiceResponse(savedStockMovement, savedStockMovementVaccineInInvoice);

    }


    public CreateStockMovementResponse createStockMovement(CreateStockMovementRequest request) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(request.getInventoryItemId()).
                orElseThrow(() -> new RuntimeException("InventoryItem Not Found"));


        inventoryItem.setQuantity(adjustQuantity(request.getMovementType(), request.getQuantity(), inventoryItem.getQuantity()));
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);

        StockMovement stockMovement = new StockMovement();
        stockMovement.setInventoryItem(savedInventoryItem);
        stockMovement.setQuantity(request.getQuantity());
        stockMovement.setPrice(request.getPrice());
        stockMovement.setMovementType(request.getMovementType());
        stockMovement.setCreatedAt(LocalDate.now());
        stockMovement.setUpdatedAt(LocalDate.now());
        stockMovement.setMovementDate(request.getMovementDate());
        stockMovement.setNotes(request.getNotes());
        StockMovement savedStockMovement = stockMovementRepository.save(stockMovement);
        return convertToCreateStockMovementResponse(savedStockMovement, "StockMovement created successfully");
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



    private CreateStockMovementPrescriptionItemResponse  convertToStockMovementPrescriptionItemResponse(StockMovement stockMovement, StockMovement_PrescriptionItem  prescriptionItem) {
        return CreateStockMovementPrescriptionItemResponse.builder()
                .id(stockMovement.getId())
                .movementType(stockMovement.getMovementType())
                .quantity(stockMovement.getQuantity())
                .price(stockMovement.getPrice())
                .movementDate(stockMovement.getMovementDate())
                .notes(stockMovement.getNotes())
                .createdAt(stockMovement.getCreatedAt())
                .updatedAt(stockMovement.getUpdatedAt())
                .inventoryItemId(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getId(): null)
                .inventoryItemName(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getName() : null)
                .inventoryItemUnit(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getUnit() : null)
                .prescriptionItemDosage(prescriptionItem.getPrescriptionItem() != null ? prescriptionItem.getPrescriptionItem().getDosage() : null)
                .prescriptionItemInstruction(prescriptionItem.getPrescriptionItem() != null ? prescriptionItem.getPrescriptionItem().getInstruction() : null)
                .prescriptionItemDuration(prescriptionItem.getPrescriptionItem() != null ? prescriptionItem.getPrescriptionItem().getDuration() : null)
                .message("")
                .build();
    }
    private CreateStockMovementPrescriptionItemResponse  convertToStockMovementPrescriptionItemResponse(StockMovement stockMovement, StockMovement_PrescriptionItem  prescriptionItem, String message) {
        return CreateStockMovementPrescriptionItemResponse.builder()
                .id(stockMovement.getId())
                .movementType(stockMovement.getMovementType())
                .quantity(stockMovement.getQuantity())
                .price(stockMovement.getPrice())
                .movementDate(stockMovement.getMovementDate())
                .notes(stockMovement.getNotes())
                .createdAt(stockMovement.getCreatedAt())
                .updatedAt(stockMovement.getUpdatedAt())
                .inventoryItemId(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getId(): null)
                .inventoryItemName(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getName() : null)
                .inventoryItemUnit(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getUnit() : null)
                .prescriptionItemDosage(prescriptionItem.getPrescriptionItem() != null ? prescriptionItem.getPrescriptionItem().getDosage() : null)
                .prescriptionItemInstruction(prescriptionItem.getPrescriptionItem() != null ? prescriptionItem.getPrescriptionItem().getInstruction() : null)
                .prescriptionItemDuration(prescriptionItem.getPrescriptionItem() != null ? prescriptionItem.getPrescriptionItem().getDuration() : null)
                .message(message)
                .build();
    }
    private CreateStockMovementProductResponse convertToStockMovementProductInInvoiceResponse(StockMovement stockMovement, StockMovement_ProductInInvoice stockMovement_ProductInInvoice) {
        return CreateStockMovementProductResponse.builder()
                .id(stockMovement.getId())
                .movementType(stockMovement.getMovementType())
                .quantity(stockMovement.getQuantity())
                .price(stockMovement.getPrice())
                .movementDate(stockMovement.getMovementDate())
                .notes(stockMovement.getNotes())
                .createdAt(stockMovement.getCreatedAt())
                .updatedAt(stockMovement.getUpdatedAt())
                .inventoryItemId(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getId(): null)
                .inventoryItemName(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getName() : null)
                .inventoryItemUnit(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getUnit() : null)
                .productInInvoiceId(stockMovement_ProductInInvoice.getProductInInvoice() != null ? stockMovement_ProductInInvoice.getProductInInvoice().getId() : null)
                .productInInvoiceName(stockMovement_ProductInInvoice.getProductInInvoice().getProduct() != null ? stockMovement_ProductInInvoice.getProductInInvoice().getProduct().getName() : null)
                .productInInvoiceUnit(stockMovement_ProductInInvoice.getProductInInvoice().getProduct() != null ?  stockMovement_ProductInInvoice.getProductInInvoice().getProduct().getUnit() : null)
                .productInInvoiceBrand(stockMovement_ProductInInvoice.getProductInInvoice().getProduct() != null ? stockMovement_ProductInInvoice.getProductInInvoice().getProduct().getBrand() : null)
                .productInInvoiceCategory(stockMovement_ProductInInvoice.getProductInInvoice().getProduct() != null ? stockMovement_ProductInInvoice.getProductInInvoice().getProduct().getCategory() : null)
                .productInInvoiceSupplier(stockMovement_ProductInInvoice.getProductInInvoice().getProduct() != null ? stockMovement_ProductInInvoice.getProductInInvoice().getProduct().getSupplier() : null)
                .message("")
                .build();
    }
    private CreateStockMovementProductResponse convertToStockMovementProductInInvoiceResponse(StockMovement stockMovement, StockMovement_ProductInInvoice stockMovement_ProductInInvoice, String message) {
        return CreateStockMovementProductResponse.builder()
                .id(stockMovement.getId())
                .movementType(stockMovement.getMovementType())
                .quantity(stockMovement.getQuantity())
                .price(stockMovement.getPrice())
                .movementDate(stockMovement.getMovementDate())
                .notes(stockMovement.getNotes())
                .createdAt(stockMovement.getCreatedAt())
                .updatedAt(stockMovement.getUpdatedAt())
                .inventoryItemId(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getId(): null)
                .inventoryItemName(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getName() : null)
                .inventoryItemUnit(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getUnit() : null)
                .productInInvoiceId(stockMovement_ProductInInvoice.getProductInInvoice() != null ? stockMovement_ProductInInvoice.getProductInInvoice().getId() : null)
                .productInInvoiceName(stockMovement_ProductInInvoice.getProductInInvoice().getProduct() != null ? stockMovement_ProductInInvoice.getProductInInvoice().getProduct().getName() : null)
                .productInInvoiceUnit(stockMovement_ProductInInvoice.getProductInInvoice().getProduct() != null ?  stockMovement_ProductInInvoice.getProductInInvoice().getProduct().getUnit() : null)
                .productInInvoiceBrand(stockMovement_ProductInInvoice.getProductInInvoice().getProduct() != null ? stockMovement_ProductInInvoice.getProductInInvoice().getProduct().getBrand() : null)
                .productInInvoiceCategory(stockMovement_ProductInInvoice.getProductInInvoice().getProduct() != null ? stockMovement_ProductInInvoice.getProductInInvoice().getProduct().getCategory() : null)
                .productInInvoiceSupplier(stockMovement_ProductInInvoice.getProductInInvoice().getProduct() != null ? stockMovement_ProductInInvoice.getProductInInvoice().getProduct().getSupplier() : null)
                .message(message)
                .build();
    }
    private CreateStockMovementVaccineResponse convertToStockMovementVaccineInInvoiceResponse(StockMovement stockMovement, StockMovement_VaccineInInvoice stockMovement_VaccineInInvoice) {
        return CreateStockMovementVaccineResponse.builder()
                .id(stockMovement.getId())
                .movementType(stockMovement.getMovementType())
                .quantity(stockMovement.getQuantity())
                .price(stockMovement.getPrice())
                .movementDate(stockMovement.getMovementDate())
                .notes(stockMovement.getNotes())
                .createdAt(stockMovement.getCreatedAt())
                .updatedAt(stockMovement.getUpdatedAt())
                .inventoryItemId(stockMovement.getInventoryItem() !=  null ? stockMovement.getInventoryItem().getId(): null)
                .inventoryItemName(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getName() : null)
                .inventoryItemUnit(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getUnit() : null)
                .vaccineName(stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine() != null ? stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine().getName() : null)
                .vaccineDescription(stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine() != null ? stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine().getDescription() : null)
                .vaccineManufacturer(stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine() != null ? stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine().getManufacturer() : null)
                .message("")
                .build();

    }

    private CreateStockMovementVaccineResponse convertToStockMovementVaccineInInvoiceResponse(StockMovement stockMovement, StockMovement_VaccineInInvoice stockMovement_VaccineInInvoice, String message) {
        return CreateStockMovementVaccineResponse.builder()
                .id(stockMovement.getId())
                .movementType(stockMovement.getMovementType())
                .quantity(stockMovement.getQuantity())
                .price(stockMovement.getPrice())
                .movementDate(stockMovement.getMovementDate())
                .notes(stockMovement.getNotes())
                .createdAt(stockMovement.getCreatedAt())
                .updatedAt(stockMovement.getUpdatedAt())
                .inventoryItemId(stockMovement.getInventoryItem() !=  null ? stockMovement.getInventoryItem().getId(): null)
                .inventoryItemName(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getName() : null)
                .inventoryItemUnit(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getUnit() : null)
                .vaccineName(stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine() != null ? stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine().getName() : null)
                .vaccineDescription(stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine() != null ? stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine().getDescription() : null)
                .vaccineManufacturer(stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine() != null ? stockMovement_VaccineInInvoice.getVaccineInInvoice().getVaccine().getManufacturer() : null)
                .message(message)
                .build();

    }

    private CreateStockMovementResponse convertToCreateStockMovementResponse(StockMovement stockMovement, String message) {
        return CreateStockMovementResponse.builder()
                .id(stockMovement.getId())
                .movementType(stockMovement.getMovementType())
                .quantity(stockMovement.getQuantity())
                .price(stockMovement.getPrice())
                .movementDate(stockMovement.getMovementDate())
                .notes(stockMovement.getNotes())
                .createdAt(stockMovement.getCreatedAt())
                .updatedAt(stockMovement.getUpdatedAt())
                .inventoryItemId(stockMovement.getInventoryItem() != null ?  stockMovement.getInventoryItem().getId(): null)
                .inventoryItemName(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getName() : null)
                .inventoryItemUnit(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getUnit() : null)
                .message(message)
                .build();
    }

    private CreateStockMovementResponse convertToCreateStockMovementResponse(StockMovement stockMovement) {
        return CreateStockMovementResponse.builder()
                .id(stockMovement.getId())
                .movementType(stockMovement.getMovementType())
                .quantity(stockMovement.getQuantity())
                .price(stockMovement.getPrice())
                .movementDate(stockMovement.getMovementDate())
                .notes(stockMovement.getNotes())
                .createdAt(stockMovement.getCreatedAt())
                .updatedAt(stockMovement.getUpdatedAt())
                .inventoryItemId(stockMovement.getInventoryItem() != null ?  stockMovement.getInventoryItem().getId(): null)
                .inventoryItemName(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getName() : null)
                .inventoryItemUnit(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getUnit() : null)
                .message("")
                .build();
    }


    private StockMovementResponse convertToStockMovementResponse(StockMovement stockMovement) {
        return StockMovementResponse.builder()
                .id(stockMovement.getId())
                .movementType(stockMovement.getMovementType())
                .quantity(stockMovement.getQuantity())
                .price(stockMovement.getPrice())
                .movementDate(stockMovement.getMovementDate())
                .createdAt(stockMovement.getCreatedAt())
                .inventoryItemId(stockMovement.getInventoryItem() != null ?  stockMovement.getInventoryItem().getId(): null)
                .inventoryItemName(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getName() : null)
                .inventoryItemType(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getInventoryType() : null)
                .notes(stockMovement.getNotes())
                .message("")
                .build();
    }
    private StockMovementResponse convertToStockMovementResponse(StockMovement stockMovement, String message) {
        return StockMovementResponse.builder()
                .id(stockMovement.getId())
                .movementType(stockMovement.getMovementType())
                .quantity(stockMovement.getQuantity())
                .price(stockMovement.getPrice())
                .movementDate(stockMovement.getMovementDate())
                .createdAt(stockMovement.getCreatedAt())
                .inventoryItemId(stockMovement.getInventoryItem() != null ?  stockMovement.getInventoryItem().getId(): null)
                .inventoryItemName(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getName() : null)
                .inventoryItemType(stockMovement.getInventoryItem() != null ? stockMovement.getInventoryItem().getInventoryType() : null)
                .notes(stockMovement.getNotes())
                .message(message)
                .build();
    }



}

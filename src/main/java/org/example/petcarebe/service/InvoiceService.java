package org.example.petcarebe.service;

import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.petcarebe.dto.request.invoice.*;
import org.example.petcarebe.dto.request.payment.ConfirmPaymentFailedRequest;
import org.example.petcarebe.dto.request.payment.ConfirmPaymentSuccessRequest;
import org.example.petcarebe.dto.request.payment.CreatePaymentRequest;
import org.example.petcarebe.dto.response.invoice.*;
import org.example.petcarebe.dto.response.payment.CreatePaymentResponse;
import org.example.petcarebe.dto.response.payment.PaymentResponse;
import org.example.petcarebe.dto.response.prescription.PrescriptionItemResponse;
import org.example.petcarebe.dto.response.prescription.PrescriptionResponse;
import org.example.petcarebe.dto.response.vaccine.VaccineInInvoiceResponse;
import org.example.petcarebe.enums.InvoiceStatus;
import org.example.petcarebe.enums.PaymentStatus;
import org.example.petcarebe.enums.StockMovementType;
import org.example.petcarebe.model.*;
import org.example.petcarebe.model.dto.PrescriptionItemDTO;
import org.example.petcarebe.payment.PaymentRequest;
import org.example.petcarebe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Value("${invoice.cacellation.deadline}")
    private Integer invoiceDeadline;

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private InvoiceDiscountRepository invoiceDiscountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServicePackageRepository servicePackageRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VaccineRepository vaccineRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private PrescriptionItemRepository  prescriptionItemRepository;
    @Autowired
    private ProductInInvoiceRepository productInInvoiceRepository;
    @Autowired
    private VaccineInInvoiceRepository  vaccineInInvoiceRepository;
    @Autowired
    private ServiceInInvoiceRepository serviceInInvoiceRepository;
    @Autowired
    private ServicePackageInInvoiceRepository servicePackageInInvoiceRepository;
    @Autowired
    private StockMovementRepository stockMovementRepository;
    @Autowired
    private StockMovement_PrescriptionItemRepository stockMovement_PrescriptionItemRepository;
    @Autowired
    private StockMovement_ProductInInvoiceRepository stockMovement_ProductInInvoiceRepository;
    @Autowired
    private StockMovement_VaccineInInvoiceRepository  stockMovement_VaccineInInvoiceRepository;
    @Autowired
    private ServicePriceHistoryRepository  servicePriceHistoryRepository;
    @Autowired
    private ServicePackagePriceHistoryRepository servicePackagePriceHistoryRepository;
    @Autowired
    private ProductPriceHistoryRepository productPriceHistoryRepository;
    @Autowired
    private VaccinePriceHistoryRepository vaccinePriceHistoryRepository;
    @Autowired
    private MedicinePriceHistoryRepository medicinePriceHistoryRepository;

    @Autowired
    private ServiceInPromotionRepository serviceInPromotionRepository;
    @Autowired
    private ServicePackageInPromotionRepository servicePackageInPromotionRepository;
    @Autowired
    private ProductInPromotionRepository productInPromotionRepository;
    @Autowired
    private VaccineInPromotionRepository vaccineInPromotionRepository;
    @Autowired
    private MedicineInPromotionRepository medicineInPromotionRepository;

    @Autowired
    private InventoryObjectRepository  inventoryObjectRepository;
    @Autowired
    private InventoryItemRepository  inventoryItemRepository;

    public CreateInvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Invoice invoice = new Invoice();
        Staff staff = staffRepository.findById(request.getStaffId() != null ? request.getStaffId() : 0).orElse(null);
        Customer customer = customerRepository.findById(request.getCustomerId() != null ? request.getCustomerId() : 0).orElse(null);

        invoice.setStaff(staff);
        invoice.setCustomer(customer);
        invoice.setCustomerName(request.getCustomerName());
        invoice.setCustomerPhone(request.getCustomerPhone());
        invoice.setNotes(request.getNotes());
        invoice.setCreatedDate(request.getCreatedDate());
        invoice.setStatus(InvoiceStatus.DRAFT);

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convertToCreateInvoiceResponse(savedInvoice);

    }
    public InvoiceResponse updateStatus(InvoiceStatus invoiceStatus, Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id:m " + invoiceId));
        invoice.setStatus(invoiceStatus);
        Invoice savedInvoice = invoiceRepository.save(invoice);
//        checkPriceInvoice(savedInvoice.getId());
        return convertToResponse(savedInvoice);
    }

    public InvoiceResponse getInvoiceById(Long invoiceId) {
        Invoice invoiceById = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        return convertToResponse(invoiceById);
    }

    public List<InvoiceResponse> getAllInvoice() {
        return invoiceRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<InvoiceResponse> getInvoicesByParam(InvoiceStatus status, LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null) {
            fromDate = LocalDate.of(1970, 1, 1); // hoặc ngày nhỏ nhất
        }
        if (toDate == null) {
            toDate = LocalDate.now(); // lấy ngày hiện tại
        }
        if(status != null) return invoiceRepository.findAllByStatusAndCreatedDateBetween(status, fromDate, toDate, Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(this::convertToResponse)
                .toList();
        return invoiceRepository.findAllByCreatedDateBetween(fromDate, toDate, Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public UpdateInvoiceResponse updateInvoice(Long invoiceId, UpdateInvoiceRequest request) {
        Invoice invoiceById = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("invoice not found"));
        Staff staff = staffRepository.findById(request.getStaffId()).orElse(null);
        Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);

        invoiceById.setStaff(staff);
        invoiceById.setCustomer(customer);
        invoiceById.setCustomerName(request.getCustomerName());
        invoiceById.setCustomerPhone(request.getCustomerPhone());
        invoiceById.setNotes(request.getNotes());

        Invoice  updatedInvoice = invoiceRepository.save(invoiceById);
        return convertToUpdateInvoiceResponse(updatedInvoice);
    }

    public CancelInvoiceResponse cancelInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("invoice not found"));
        Prescription prescription = prescriptionRepository.findByInvoice(invoice).orElse(null);
        if(prescription != null) {
            List<PrescriptionItem> prescriptionItemList = prescriptionItemRepository.findAllByPrescription(prescription);
            for(PrescriptionItem prescriptionItem : prescriptionItemList) {
                List<StockMovement_PrescriptionItem> stockMovementPrescriptionItems = stockMovement_PrescriptionItemRepository.findAllByPrescriptionItem(prescriptionItem);
                for(StockMovement_PrescriptionItem  stockMovementPrescriptionItem : stockMovementPrescriptionItems) {
                    StockMovement stockMovement = stockMovementPrescriptionItem.getStockMovement();
                    stockMovement_PrescriptionItemRepository.delete(stockMovementPrescriptionItem);
                    stockMovementRepository.delete(stockMovement);
                }
            }
        }
        List<ProductInInvoice> productInInvoiceList = productInInvoiceRepository.findAllByInvoice(invoice);
        if(productInInvoiceList != null) {
            if(!productInInvoiceList.isEmpty()) {
                for(ProductInInvoice productInInvoice : productInInvoiceList) {
                    List<StockMovement_ProductInInvoice> stockMovementProductInInvoices = stockMovement_ProductInInvoiceRepository.findAllByProductInInvoice(productInInvoice);
                    for(StockMovement_ProductInInvoice stockMovementProductInInvoice : stockMovementProductInInvoices) {
                        StockMovement stockMovement = stockMovementProductInInvoice.getStockMovement();
                        stockMovement_ProductInInvoiceRepository.delete(stockMovementProductInInvoice);
                        stockMovementRepository.delete(stockMovement);
                    }
                }
            }
        }
        List<VaccineInInvoice> vaccineInInvoiceList = vaccineInInvoiceRepository.findAllByInvoice(invoice);
        if(vaccineInInvoiceList != null) {
            if(!vaccineInInvoiceList.isEmpty()) {
                for(VaccineInInvoice vaccineInInvoice: vaccineInInvoiceList) {
                    List<StockMovement_VaccineInInvoice> stockMovementVaccineInInvoices = stockMovement_VaccineInInvoiceRepository.findAllByVaccineInInvoice(vaccineInInvoice);
                    for (StockMovement_VaccineInInvoice stockMovementVaccineInInvoice : stockMovementVaccineInInvoices) {
                        StockMovement stockMovement = new StockMovement();
                        stockMovement_VaccineInInvoiceRepository.delete(stockMovementVaccineInInvoice);
                        stockMovementRepository.delete(stockMovement);
                    }
                }
            }
        }
        // Hoanf tieenf, la,mf sau
        //Xoas ServicePackageInInvoice, ServiceInInvoice, VaccineInInvoice, Prescription, ProductInInvoice, Payment,  InvoiceDiscount
        //set status = CANCELLED
        invoice.setStatus(InvoiceStatus.CANCELLED);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convertToCancelInvoiceResponse(savedInvoice, "Invoice cancelled successfully");

    }
    public List<InvoiceResponse> getByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("customer not found"));

        List<Invoice> invoiceList = invoiceRepository.getInvoicesByCustomer(customer);
        return invoiceList.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public InvoiceItemsResponse getInvoiceItems(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("invoice not found"));
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findByInvoiceId(invoice.getId());

        System.out.println("DOn thuoc trong hoa don " + prescriptionOptional.isEmpty());

        List<PrescriptionItem> prescriptionItems = new ArrayList<>(); //
        List<ProductInInvoice> productInInvoiceList;
        List<ServiceInInvoice> serviceInInvoiceList = new ArrayList<>();
        List<VaccineInInvoice> vaccineInInvoiceList = new ArrayList<>();
        List<InvoiceDiscount>  invoiceDiscountList = new ArrayList<>();
        List<ServicePackageInInvoice>  servicePackageInInvoiceList = new ArrayList<>();
        List<Payment> paymentList = new ArrayList<>();
        Prescription prescription = new Prescription();
        if(prescriptionOptional.isPresent()) {
            prescription = prescriptionOptional.get();
            prescriptionItems = prescriptionItemRepository.findAllByPrescription(prescription);
        }
        productInInvoiceList = productInInvoiceRepository.findAllByInvoice(invoice);
        serviceInInvoiceList = serviceInInvoiceRepository.findAllByInvoice(invoice);
        servicePackageInInvoiceList = servicePackageInInvoiceRepository.findAllByInvoice(invoice);
        invoiceDiscountList = invoiceDiscountRepository.findAllByInvoice(invoice);
        vaccineInInvoiceList = vaccineInInvoiceRepository.findAllByInvoice(invoice);
        paymentList = paymentRepository.findAllByInvoice(invoice);
        return convertToInvoiceItemsResponse(
                invoiceId,
                paymentList,
                productInInvoiceList,
                servicePackageInInvoiceList,
                serviceInInvoiceList,
                invoiceDiscountList,
                prescription,
                prescriptionItems,
                vaccineInInvoiceList,
                "InvoiceItems found successfully"
                );
    }
    // Your service methods will go here
    public AddServiceToInvoiceResponse addServiceToInvoice(Long invoiceId, AddServiceToInvoiceRequest request) {
        Invoice invoiceById = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("invoice not found"));
        org.example.petcarebe.model.Service serviceById = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        ServicePriceHistory servicePriceHistory = servicePriceHistoryRepository.findByServiceAndStatus(serviceById, "ACTIVE")
                .orElseThrow(() -> new RuntimeException("Service Price History not found"));

        ServiceInPromotion serviceInPromotion = serviceInPromotionRepository.findByService(serviceById)
                .orElse(null);


        ServiceInInvoice serviceInInvoice = new ServiceInInvoice();
        serviceInInvoice.setService(serviceById);
        serviceInInvoice.setInvoice(invoiceById);
        serviceInInvoice.setPrice(servicePriceHistory.getPrice());
        serviceInInvoice.setQuantity(request.getQuantity());
        serviceInInvoice.setTaxPercent(request.getTaxPercent());
        //thieeu promotionAmount
        if(serviceInPromotion != null) {
            if(serviceInPromotion.getPromotion() != null){
                if(serviceInPromotion.getPromotion().getType() == "CASH"){
                    serviceInInvoice.setPromotionAmount(serviceInPromotion.getPromotion().getValue());
                }
                else if(serviceInPromotion.getPromotion().getType() == "PERCENT"){
                    serviceInInvoice.setPromotionAmount(serviceInPromotion.getPromotion().getValue()*servicePriceHistory.getPrice()/100);
                }
            }
        }
        serviceInInvoice.setNotes(request.getNotes());
        ServiceInInvoice savedServiceInInvoice = serviceInInvoiceRepository.save(serviceInInvoice);

        return convertToAddServiceInvoiceResponse(savedServiceInInvoice, "Service added to Invoice successfully");
    }

    public AddServicePackageToInvoiceResponse addServicePackageToInvoice(Long invoiceId, AddServicePackageToInvoiceRequest request) {
        Invoice invoiceById = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("invoice not found"));
        ServicePackage servicePackageById = servicePackageRepository.findById(request.getServicePackageId())
                .orElseThrow(() -> new RuntimeException("Service package not found"));

        ServicePackagePriceHistory  servicePackagePriceHistory = servicePackagePriceHistoryRepository.findByServicePackageAndStatus(servicePackageById, "ACTIVE")
                .orElseThrow(() -> new RuntimeException("Service Package Price History not found"));

        ServicePackageInPromotion servicePackageInPromotion = servicePackageInPromotionRepository.findByServicePackage(servicePackageById)
                .orElse(null);

        ServicePackageInInvoice servicePackageInInvoice = new ServicePackageInInvoice();
        servicePackageInInvoice.setServicePackage(servicePackageById);
        servicePackageInInvoice.setInvoice(invoiceById);
        servicePackageInInvoice.setPrice(servicePackagePriceHistory.getPrice());
        servicePackageInInvoice.setQuantity(request.getQuantity());
        servicePackageInInvoice.setTaxPercent(request.getTaxPercent());
        servicePackageInInvoice.setNotes(request.getNotes());
        if(servicePackageInPromotion != null) {
            if(servicePackageInPromotion.getPromotion() != null){
                if(servicePackageInPromotion.getPromotion().getType() == "CASH"){
                    servicePackageInInvoice.setPromotionAmount(servicePackageInPromotion.getPromotion().getValue());
                }
                else if(servicePackageInPromotion.getPromotion().getType() == "PERCENT"){
                    servicePackageInInvoice.setPromotionAmount(servicePackageInPromotion.getPromotion().getValue()*servicePackagePriceHistory.getPrice()/100);
                }
            }
        }
        ServicePackageInInvoice savedServicePackageInInvoice = servicePackageInInvoiceRepository.save(servicePackageInInvoice);
        return convertToAddServicePackageInvoiceResponse(savedServicePackageInInvoice, "Service Package InInvoice Created successfullt");
    }

    public AddProductToInvoiceResponse addProductToInvoice(Long invoiceId, AddProductToInvoiceRequest request) {
        Invoice invoiceById = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("invoice not found"));

        Product productById = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("product not found"));

        ProductPriceHistory productPriceHistory = productPriceHistoryRepository.findByProductAndStatus(productById, "ACTIVE")
                .orElseThrow(() -> new RuntimeException("product price not found"));
        ProductInPromotion productInPromotion = productInPromotionRepository.findByProduct(productById)
                .orElse(null);

//        InventoryObject inventoryObjectByProduct = inventoryObjectRepository.findById(productById.getInventoryObject().getId())
//                .orElseThrow(() -> new RuntimeException("Inventory Object Of Product not found"));
        InventoryObject inventoryObjectByProduct = productById.getInventoryObject();
        InventoryItem inventoryItemByProduct = inventoryItemRepository.findByInventoryObject(inventoryObjectByProduct)
                .orElseThrow(() -> new RuntimeException("Inventory Item Of Product not found"));

        inventoryItemByProduct.setQuantity(adjustQuantity(StockMovementType.OUT, request.getQuantity(), inventoryItemByProduct.getQuantity()));
        InventoryItem savedInventory = inventoryItemRepository.save(inventoryItemByProduct);
        StockMovement stockMovement = new StockMovement();
        stockMovement.setQuantity(request.getQuantity());
        stockMovement.setMovementType(StockMovementType.SALE);
        stockMovement.setCreatedAt(LocalDate.now());
        stockMovement.setPrice(productPriceHistory.getPrice());
        stockMovement.setMovementDate(LocalDateTime.now());
        stockMovement.setInventoryItem(savedInventory);

        StockMovement savedStockMovement = stockMovementRepository.save(stockMovement);

        ProductInInvoice productInInvoice = new ProductInInvoice();
        productInInvoice.setInvoice(invoiceById);
        productInInvoice.setProduct(productById);
        productInInvoice.setPrice(productPriceHistory.getPrice());
        productInInvoice.setQuantity(request.getQuantity());
        productInInvoice.setTaxPercent(request.getTaxPercent());
        productInInvoice.setNotes(request.getNotes());
        if(productInPromotion != null) {
            if(productInPromotion.getPromotion() != null){
                if(productInPromotion.getPromotion().getType() == "CASH"){
                    productInInvoice.setPromotionAmount(productInPromotion.getPromotion().getValue());
                }
                else if(productInPromotion.getPromotion().getType() == "PERCENT"){
                    productInInvoice.setPromotionAmount(productInPromotion.getPromotion().getValue()*productPriceHistory.getPrice()/100);
                }
            }
        }
        ProductInInvoice savedProductInInvoice = productInInvoiceRepository.save(productInInvoice);

        StockMovement_ProductInInvoice stockMovementProductInInvoice = new StockMovement_ProductInInvoice();
        stockMovementProductInInvoice.setStockMovement(savedStockMovement);
        stockMovementProductInInvoice.setProductInInvoice(savedProductInInvoice);

        StockMovement_ProductInInvoice savedStockMovementProductInInvoice = stockMovement_ProductInInvoiceRepository.save(stockMovementProductInInvoice);

        return convertToAddProductToInvoiceResponse(savedProductInInvoice, "Product In Invoice Created successfullt");
    }

    public AddVaccineToInvoiceResponse addVaccineToInvoice(Long invoiceId, AddVaccineToInvoiceRequest request){
        Invoice invoiceById = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("invoice not found"));
        Vaccine vaccineById = vaccineRepository.findById(request.getVaccineId())
                .orElseThrow(() -> new RuntimeException("vaccine not found"));
        VaccinePriceHistory  vaccinePriceHistory = vaccinePriceHistoryRepository.findByVaccineAndStatus(vaccineById, "ACTIVE")
                .orElseThrow(()  -> new RuntimeException("vaccine Price History not found"));
        VaccineInPromotion vaccineInPromotion = vaccineInPromotionRepository.findByVaccine(vaccineById)
                .orElseThrow(() -> new RuntimeException("vaccine In Promotion not found"));

//        InventoryObject inventoryObjectByVaccine = inventoryObjectRepository.findById(vaccineById.getInventoryObject().getId())
//                .orElseThrow(() -> new RuntimeException("Inventory Object Of Vaccine not found"));
        InventoryObject inventoryObjectByVaccine = vaccineById.getInventoryObject();
        InventoryItem inventoryItemByVaccine = inventoryItemRepository.findByInventoryObject(inventoryObjectByVaccine)
                .orElseThrow(() -> new RuntimeException("Inventory Item Of Vaccine not found"));

        inventoryItemByVaccine.setQuantity(adjustQuantity(StockMovementType.OUT, request.getQuantity(), inventoryItemByVaccine.getQuantity()));
        InventoryItem savedInventory = inventoryItemRepository.save(inventoryItemByVaccine);

        StockMovement stockMovement = new StockMovement();
        stockMovement.setQuantity(request.getQuantity());
        stockMovement.setMovementType(StockMovementType.SALE);
        stockMovement.setCreatedAt(LocalDate.now());
        stockMovement.setPrice(vaccinePriceHistory.getPrice());
        stockMovement.setMovementDate(LocalDateTime.now());
        stockMovement.setInventoryItem(savedInventory);

        StockMovement savedStockMovement = stockMovementRepository.save(stockMovement);
        VaccineInInvoice vaccineInInvoice = new VaccineInInvoice();
        vaccineInInvoice.setInvoice(invoiceById);
        vaccineInInvoice.setVaccine(vaccineById);
        vaccineInInvoice.setPrice(vaccinePriceHistory.getPrice());
        vaccineInInvoice.setQuantity(request.getQuantity());
        vaccineInInvoice.setTaxPercent(request.getTaxPercent());
        vaccineInInvoice.setNotes(request.getNotes());
        if(vaccineInPromotion != null) {
            if(vaccineInPromotion.getPromotion() != null){
                if(vaccineInPromotion.getPromotion().getType() == "CASH"){
                    vaccineInInvoice.setPromotionAmount(vaccineInPromotion.getPromotion().getValue());
                }
                else if(vaccineInPromotion.getPromotion().getType() == "PERCENT"){
                    vaccineInInvoice.setPromotionAmount(vaccineInPromotion.getPromotion().getValue()*vaccinePriceHistory.getPrice()/100);
                }
            }
        }
        VaccineInInvoice savedvaccineInInvoice= vaccineInInvoiceRepository.save(vaccineInInvoice);

        StockMovement_VaccineInInvoice stockMovementVaccineInInvoice = new StockMovement_VaccineInInvoice();
        stockMovementVaccineInInvoice.setStockMovement(savedStockMovement);
        stockMovementVaccineInInvoice.setVaccineInInvoice(savedvaccineInInvoice);
        stockMovement_VaccineInInvoiceRepository.save(stockMovementVaccineInInvoice);

        return convertToAddVaccineToInvoiceResponse(savedvaccineInInvoice);
    }

//    public AddPromotionToInvoiceResponse addPromotionToInvoice(AddPromotionToInvoiceRequest request) {
//    }

    public AddPrescriptionToInvoiceResponse addPrescriptionToInvoice(Long invoiceId, AddPrescriptionToInvoiceRequest request) {
        Invoice invoiceById = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("invoice not found"));

        Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("prescription not found"));

        prescription.setInvoice(invoiceById);
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return convertToAddPrescriptionToInvoiceResponse(savedPrescription);
    }
    public InvoiceResponse checkPriceInvoice(Long invoiceId) {
        Invoice invoiceById =  invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("invoice not found"));

        List<ServiceInInvoice> serviceInInvoices = serviceInInvoiceRepository.findAllByInvoice(invoiceById);
        List<ServicePackageInInvoice> servicePackageInInvoices = servicePackageInInvoiceRepository.findAllByInvoice(invoiceById);
        List<ProductInInvoice> productInInvoices = productInInvoiceRepository.findAllByInvoice(invoiceById);
        List<VaccineInInvoice> vaccineInInvoices = vaccineInInvoiceRepository.findAllByInvoice(invoiceById);
        Prescription prescriptionByInvoiceOptional= prescriptionRepository.findByInvoice(invoiceById).orElse(null);
        List<InvoiceDiscount> invoiceDiscounts = invoiceDiscountRepository.findAllByInvoice(invoiceById);
        List<PrescriptionItemDTO> prescriptionItems = new ArrayList<>();
        if(prescriptionByInvoiceOptional != null){
            System.out.println("maDOnthuoc: " + prescriptionByInvoiceOptional.getId());
            try {
                prescriptionItems = prescriptionItemRepository.findAllByPrescriptionId(prescriptionByInvoiceOptional.getId());
            } catch (Exception e) {
                System.out.println("Loi đoan prescriptionId" + e.getMessage());
            }
        }
        Double totalAmount = getTotalAmount(serviceInInvoices) + getTotalAmount(servicePackageInInvoices)
                + getTotalAmountPrescriptionItem(prescriptionItems) + getTotalAmount(productInInvoices) + getTotalAmount(vaccineInInvoices);
        Double totalDiscountAmount = getTotalDiscountAmount(invoiceDiscounts) + getPromotionAmount(serviceInInvoices)
                +getPromotionAmount(servicePackageInInvoices) + getPromotionAmountPrescriptionItem(prescriptionItems)
                + getPromotionAmount(productInInvoices) + getPromotionAmount(vaccineInInvoices);
        Double taxTotalAmount = getTaxTotalAmount(serviceInInvoices) + getTaxTotalAmount(servicePackageInInvoices)
                + getTaxTotalAmountPrescriptionItem(prescriptionItems) + getTaxTotalAmount(productInInvoices) + getTaxTotalAmount(vaccineInInvoices);
        Double finalAmount = totalAmount - totalDiscountAmount + taxTotalAmount;

        invoiceById.setTotalAmount(totalAmount);
        invoiceById.setTotalDiscountAmount(totalDiscountAmount);
        invoiceById.setTaxTotalAmount(taxTotalAmount);
        invoiceById.setFinalAmount(finalAmount);

        Invoice savedInvoice = invoiceRepository.save(invoiceById);
        return convertToResponse(savedInvoice);

    }

    // Them Discount
    public InvoiceDiscountResponse addDiscountToInvoice(Long invoiceId, AddDiscountToInvoiceRequest request) {
        Invoice invoiceById =  invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("invoice not found"));
        Discount discountById = discountRepository.findById(request.getDiscountId())
                .orElseThrow(() -> new RuntimeException("discount not found"));
        String typeDiscount = discountById.getType();
        Double appliedAmount = 0.0;
        if(typeDiscount.equals("CASH")){
            appliedAmount = discountById.getValue() > discountById.getMinAmount() ?
                    (discountById.getValue() < discountById.getMaxAmount() ? discountById.getValue() : discountById.getMaxAmount())
                    : discountById.getMinAmount();
        }
        else{
            appliedAmount = invoiceById.getTotalAmount() * discountById.getValue() / 100;
            appliedAmount = appliedAmount > discountById.getMinAmount() ?
                    (appliedAmount < discountById.getMaxAmount() ? appliedAmount : discountById.getMaxAmount())
                    : discountById.getMinAmount();
        }
        InvoiceDiscount invoiceDiscount = new InvoiceDiscount();
        invoiceDiscount.setDiscount(discountById);
        invoiceDiscount.setAppliedAmount(appliedAmount);
        invoiceDiscount.setInvoice(invoiceById);
        InvoiceDiscount savedInvoiceDiscount = invoiceDiscountRepository.save(invoiceDiscount);
        return convertToInvoiceDiscount(savedInvoiceDiscount, "Discount added to Invoice successfully");
    }


    private Double getTotalAmount(List<? extends AbstractInvoiceItem> items) {
        if (items == null || items.isEmpty()) return 0.0;
        return items.stream()
                .mapToDouble(AbstractInvoiceItem::getTotalPrice)
                .sum();
    }
    private Double getTotalAmountPrescriptionItem(List<PrescriptionItemDTO> items) {
        if (items == null || items.isEmpty()) return 0.0;
        return items.stream()
                .mapToDouble(PrescriptionItemDTO::getTotalPrice)
                .sum();
    }
    private Double getTotalDiscountAmount(List<InvoiceDiscount> discounts) {
        if (discounts == null || discounts.isEmpty()) return 0.0;
        return discounts.stream().mapToDouble(InvoiceDiscount::getAppliedAmount)
                .sum();
    }
    private Double getTaxTotalAmount(List<? extends AbstractInvoiceItem> items) {
        if (items == null || items.isEmpty()) return 0.0;
        return items.stream()
                .mapToDouble(AbstractInvoiceItem::getTaxAmount)
                .sum();
    }
    private Double getTaxTotalAmountPrescriptionItem(List<PrescriptionItemDTO> items) {
        if (items == null || items.isEmpty()) return 0.0;
        return items.stream()
                .mapToDouble(PrescriptionItemDTO::getTaxAmount)
                .sum();
    }
    private Double getPromotionAmount(List<? extends AbstractInvoiceItem> items) {
        if (items == null || items.isEmpty()) return 0.0;
        return items.stream()
                .mapToDouble(AbstractInvoiceItem::getPromotionAmount)
                .sum();
    }
    private Double getPromotionAmountPrescriptionItem(List<PrescriptionItemDTO> items) {
        if (items == null || items.isEmpty()) return 0.0;
        return items.stream()
                .mapToDouble(PrescriptionItemDTO::getPromotionAmount)
                .sum();
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

    public CreatePaymentResponse createPaymentForInvoice(Long invoiceId, CreatePaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));

        PaymentResponse response = new PaymentResponse();
        Payment paymentForInvoice =  new Payment();
        paymentForInvoice.setInvoice(invoice);
        paymentForInvoice.setAmount(Double.valueOf(request.getAmount()));
        paymentForInvoice.setPaymentDate(LocalDate.now());

        return new CreatePaymentResponse();
    }


    private InvoiceResponse convertToResponse(Invoice invoice) {
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .customerName(invoice.getCustomerName())
                .customerPhone(invoice.getCustomerPhone())
                .createdDate(invoice.getCreatedDate())
                .totalAmount(invoice.getTotalAmount())
                .totalDiscountAmount(invoice.getTotalDiscountAmount())
                .taxTotalAmount(invoice.getTaxTotalAmount())
                .finalAmount(invoice.getFinalAmount())
                .status(invoice.getStatus())
                .notes(invoice.getNotes())
                .staffId(invoice.getStaff() != null ? invoice.getStaff().getId() : null)
                .staffName(invoice.getStaff() != null ? invoice.getStaff().getFullname() : null)
                .message("")
                .build();
    }

    private InvoiceResponse convertToResponse(Invoice invoice, String message) {
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .customerName(invoice.getCustomerName())
                .customerPhone(invoice.getCustomerPhone())
                .createdDate(invoice.getCreatedDate())
                .totalAmount(invoice.getTotalAmount())
                .totalDiscountAmount(invoice.getTotalDiscountAmount())
                .taxTotalAmount(invoice.getTaxTotalAmount())
                .finalAmount(invoice.getFinalAmount())
                .status(invoice.getStatus())
                .notes(invoice.getNotes())
                .staffId(invoice.getStaff() != null ? invoice.getStaff().getId() : null)
                .staffName(invoice.getStaff() != null ? invoice.getStaff().getFullname() : null)
                .message(message)
                .build();
    }
    private CreateInvoiceResponse convertToCreateInvoiceResponse(Invoice invoice) {
        return CreateInvoiceResponse.builder()
                .id(invoice.getId())
                .customerName(invoice.getCustomerName())
                .createDate(invoice.getCreatedDate())
                .status(invoice.getStatus())
                .staffId(invoice.getStaff() != null ? invoice.getStaff().getId()  : null)
                .customerId(invoice.getCustomer() != null ? invoice.getCustomer().getId()  : null)
                .customerPhone(invoice.getCustomerPhone())
                .notes(invoice.getNotes())
                .totalAmount(invoice.getTotalAmount())
                .totalDiscountAmount(invoice.getTotalDiscountAmount())
                .finalAmount(invoice.getFinalAmount())
                .message("")
                .build();
    }
    private CreateInvoiceResponse convertToCreateInvoiceResponse(Invoice invoice, String message) {
        return CreateInvoiceResponse.builder()
                .id(invoice.getId())
                .customerName(invoice.getCustomerName())
                .createDate(invoice.getCreatedDate())
                .status(invoice.getStatus())
                .staffId(invoice.getStaff() != null ? invoice.getStaff().getId()  : null)
                .customerId(invoice.getCustomer() != null ? invoice.getCustomer().getId()  : null)
                .customerPhone(invoice.getCustomerPhone())
                .notes(invoice.getNotes())
                .totalAmount(invoice.getTotalAmount())
                .totalDiscountAmount(invoice.getTotalDiscountAmount())
                .finalAmount(invoice.getFinalAmount())
                .message(message)
                .build();
    }

    private UpdateInvoiceResponse convertToUpdateInvoiceResponse(Invoice invoice) {
        return UpdateInvoiceResponse.builder()
                .id(invoice.getId())
                .customerName(invoice.getCustomerName())
                .createDate(invoice.getCreatedDate())
                .status(invoice.getStatus())
                .staffId(invoice.getStaff() != null ? invoice.getStaff().getId()  : null)
                .customerId(invoice.getCustomer() != null ? invoice.getCustomer().getId()  : null)
                .customerPhone(invoice.getCustomerPhone())
                .notes(invoice.getNotes())
                .totalAmount(invoice.getTotalAmount())
                .totalDiscountAmount(invoice.getTotalDiscountAmount())
                .finalAmount(invoice.getFinalAmount())
                .message("")
                .build();
    }
    private UpdateInvoiceResponse convertToUpdateInvoiceResponse(Invoice invoice, String message) {
        return UpdateInvoiceResponse.builder()
                .id(invoice.getId())
                .customerName(invoice.getCustomerName())
                .createDate(invoice.getCreatedDate())
                .status(invoice.getStatus())
                .staffId(invoice.getStaff() != null ? invoice.getStaff().getId()  : null)
                .customerId(invoice.getCustomer() != null ? invoice.getCustomer().getId()  : null)
                .customerPhone(invoice.getCustomerPhone())
                .notes(invoice.getNotes())
                .totalAmount(invoice.getTotalAmount())
                .totalDiscountAmount(invoice.getTotalDiscountAmount())
                .finalAmount(invoice.getFinalAmount())
                .message(message)
                .build();
    }


   private CancelInvoiceResponse convertToCancelInvoiceResponse(Invoice invoice) {
        return CancelInvoiceResponse.builder().id(invoice.getId())
                .customerName(invoice.getCustomerName())
                .customerPhone(invoice.getCustomerPhone())
                .staffName(invoice.getStaff() != null ? invoice.getStaff().getFullname()  : null)
                .status(invoice.getStatus())
                .message("")
                .build();
   }

    private CancelInvoiceResponse convertToCancelInvoiceResponse(Invoice invoice, String message) {
        return CancelInvoiceResponse.builder().id(invoice.getId())
                .customerName(invoice.getCustomerName())
                .customerPhone(invoice.getCustomerPhone())
                .staffName(invoice.getStaff() != null ? invoice.getStaff().getFullname()  : null)
                .status(invoice.getStatus())
                .message(message)
                .build();
    }

    private AddServiceToInvoiceResponse convertToAddServiceInvoiceResponse(ServiceInInvoice serviceInInvoice) {
        return AddServiceToInvoiceResponse.builder()
                .invoiceId(serviceInInvoice.getInvoice() != null ? serviceInInvoice.getInvoice().getId() : null)
                .serviceId(serviceInInvoice.getService() != null ? serviceInInvoice.getService().getId() : null)
                .serviceName(serviceInInvoice.getService() != null ? serviceInInvoice.getService().getName() : null)
                .serviceDescription(serviceInInvoice.getService() != null ? serviceInInvoice.getService().getDescription() : null)
                .serviceImgUrl(serviceInInvoice.getService() != null ? serviceInInvoice.getService().getImgUrl() : null)
                .serviceCategory(serviceInInvoice.getService() != null ? serviceInInvoice.getService().getCategory() : null)
                .servicePrice(serviceInInvoice.getPrice())
                .quantity(serviceInInvoice.getQuantity())
                .taxPercent(serviceInInvoice.getTaxPercent())
                .promotionAmount(serviceInInvoice.getPromotionAmount())
                .notes(serviceInInvoice.getNotes())
                .message("")
                .build();

    }

    private AddServiceToInvoiceResponse convertToAddServiceInvoiceResponse(ServiceInInvoice serviceInInvoice, String message) {
        return AddServiceToInvoiceResponse.builder()
                .invoiceId(serviceInInvoice.getInvoice() != null ? serviceInInvoice.getInvoice().getId() : null)
                .serviceId(serviceInInvoice.getService() != null ? serviceInInvoice.getService().getId() : null)
                .serviceName(serviceInInvoice.getService() != null ? serviceInInvoice.getService().getName() : null)
                .serviceDescription(serviceInInvoice.getService() != null ? serviceInInvoice.getService().getDescription() : null)
                .serviceImgUrl(serviceInInvoice.getService() != null ? serviceInInvoice.getService().getImgUrl() : null)
                .serviceCategory(serviceInInvoice.getService() != null ? serviceInInvoice.getService().getCategory() : null)
                .servicePrice(serviceInInvoice.getPrice())
                .quantity(serviceInInvoice.getQuantity())
                .taxPercent(serviceInInvoice.getTaxPercent())
                .promotionAmount(serviceInInvoice.getPromotionAmount())
                .notes(serviceInInvoice.getNotes())
                .message(message)
                .build();
    }

    private AddServicePackageToInvoiceResponse convertToAddServicePackageInvoiceResponse(ServicePackageInInvoice servicePackageInInvoice) {
        return AddServicePackageToInvoiceResponse.builder()
                .invoiceId(servicePackageInInvoice.getInvoice() != null ? servicePackageInInvoice.getInvoice().getId() : null)
                .servicePackageId(servicePackageInInvoice.getServicePackage() != null ? servicePackageInInvoice.getServicePackage().getId() : null)
                .servicePackageName(servicePackageInInvoice.getServicePackage() != null ? servicePackageInInvoice.getServicePackage().getName() : null)
                .servicePackageDescription(servicePackageInInvoice.getServicePackage() != null ? servicePackageInInvoice.getServicePackage().getDescription() : null)
                .servicePackageImgUrl(servicePackageInInvoice.getServicePackage() != null ? servicePackageInInvoice.getServicePackage().getImgUrl() : null)
                .servicePackagePrice(servicePackageInInvoice.getPrice())
                .quantity(servicePackageInInvoice.getQuantity())
                .taxPercent(servicePackageInInvoice.getTaxPercent())
                .promotionAmount(servicePackageInInvoice.getPromotionAmount())
                .notes(servicePackageInInvoice.getNotes())
                .message("")
                .build();
    }
    private AddServicePackageToInvoiceResponse convertToAddServicePackageInvoiceResponse(ServicePackageInInvoice servicePackageInInvoice,  String message) {
        return AddServicePackageToInvoiceResponse.builder()
                .invoiceId(servicePackageInInvoice.getInvoice() != null ? servicePackageInInvoice.getInvoice().getId() : null)
                .servicePackageId(servicePackageInInvoice.getServicePackage() != null ? servicePackageInInvoice.getServicePackage().getId() : null)
                .servicePackageName(servicePackageInInvoice.getServicePackage() != null ? servicePackageInInvoice.getServicePackage().getName() : null)
                .servicePackageDescription(servicePackageInInvoice.getServicePackage() != null ? servicePackageInInvoice.getServicePackage().getDescription() : null)
                .servicePackageImgUrl(servicePackageInInvoice.getServicePackage() != null ? servicePackageInInvoice.getServicePackage().getImgUrl() : null)
                .servicePackagePrice(servicePackageInInvoice.getPrice())
                .quantity(servicePackageInInvoice.getQuantity())
                .taxPercent(servicePackageInInvoice.getTaxPercent())
                .promotionAmount(servicePackageInInvoice.getPromotionAmount())
                .notes(servicePackageInInvoice.getNotes())
                .message(message)
                .build();
    }

    private AddProductToInvoiceResponse convertToAddProductToInvoiceResponse(ProductInInvoice productInInvoice) {
        return AddProductToInvoiceResponse.builder()
                .invoiceId(productInInvoice.getInvoice() != null ? productInInvoice.getInvoice().getId(): null)
                .productName(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getName() : null)
                .productDescription(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getDescription() : null)
                .productCategory(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getCategory() : null)
                .productImgUrl(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getImgUrl() : null)
                .productBrand(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getBrand() : null)
                .productUnit(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getUnit() : null)
                .productSupplier(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getSupplier() : null)
                .quantity(productInInvoice.getQuantity())
                .taxPercent(productInInvoice.getTaxPercent())
                .promotionAmount(productInInvoice.getPromotionAmount())
                .notes(productInInvoice.getNotes())
                .message("")
                .build();
    }
    private AddProductToInvoiceResponse convertToAddProductToInvoiceResponse(ProductInInvoice productInInvoice, String message) {
        return AddProductToInvoiceResponse.builder()
                .invoiceId(productInInvoice.getInvoice() != null ? productInInvoice.getInvoice().getId(): null)
                .productName(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getName() : null)
                .productDescription(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getDescription() : null)
                .productCategory(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getCategory() : null)
                .productImgUrl(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getImgUrl() : null)
                .productBrand(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getBrand() : null)
                .productUnit(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getUnit() : null)
                .productSupplier(productInInvoice.getProduct() != null ? productInInvoice.getProduct().getSupplier() : null)
                .quantity(productInInvoice.getQuantity())
                .taxPercent(productInInvoice.getTaxPercent())
                .promotionAmount(productInInvoice.getPromotionAmount())
                .notes(productInInvoice.getNotes())
                .message(message)
                .build();
    }

    private AddVaccineToInvoiceResponse convertToAddVaccineToInvoiceResponse(VaccineInInvoice vaccineInInvoice){
        return AddVaccineToInvoiceResponse.builder()
                .invoiceId(vaccineInInvoice != null ? vaccineInInvoice.getInvoice().getId() : null)
                .vaccineName(vaccineInInvoice != null ? vaccineInInvoice.getVaccine().getName(): null)
                .vaccineDescription(vaccineInInvoice != null ? vaccineInInvoice.getVaccine().getDescription(): null)
                .vaccineManufacturer(vaccineInInvoice != null ? vaccineInInvoice.getVaccine().getManufacturer(): null)
                .quantity(vaccineInInvoice.getQuantity())
                .taxPercent(vaccineInInvoice.getTaxPercent())
                .promotionAmount(vaccineInInvoice.getPromotionAmount())
                .notes(vaccineInInvoice.getNotes())
                .message("")
                .build();

    }

    private AddVaccineToInvoiceResponse convertToAddVaccineToInvoiceResponse(VaccineInInvoice vaccineInInvoice, String message) {
        return AddVaccineToInvoiceResponse.builder()
                .invoiceId(vaccineInInvoice != null ? vaccineInInvoice.getInvoice().getId() : null)
                .vaccineName(vaccineInInvoice != null ? vaccineInInvoice.getVaccine().getName(): null)
                .vaccineDescription(vaccineInInvoice != null ? vaccineInInvoice.getVaccine().getDescription(): null)
                .vaccineManufacturer(vaccineInInvoice != null ? vaccineInInvoice.getVaccine().getManufacturer(): null)
                .quantity(vaccineInInvoice.getQuantity())
                .taxPercent(vaccineInInvoice.getTaxPercent())
                .promotionAmount(vaccineInInvoice.getPromotionAmount())
                .notes(vaccineInInvoice.getNotes())
                .message(message)
                .build();

    }

    private AddPrescriptionToInvoiceResponse convertToAddPrescriptionToInvoiceResponse(Prescription prescription){
        return AddPrescriptionToInvoiceResponse.builder()
                .invoiceId(prescription.getInvoice() != null ? prescription.getInvoice().getId() : null)
                .prescriptionId(prescription.getId())
                .prescriptionCreatedDate(prescription.getCreatedDate())
                .prescriptionNotes(prescription.getNotes())
                .message("")
                .build();
    }
    private AddPrescriptionToInvoiceResponse convertToAddPrescriptionToInvoiceResponse(Prescription prescription, String message) {
        return AddPrescriptionToInvoiceResponse.builder()
                .invoiceId(prescription.getInvoice() != null ? prescription.getInvoice().getId() : null)
                .prescriptionId(prescription.getId())
                .prescriptionCreatedDate(prescription.getCreatedDate())
                .prescriptionNotes(prescription.getNotes())
                .message(message)
                .build();
    }

    private AddPromotionToInvoiceResponse convertToAddAddPromotionToInvoiceResponse(Long invoiceId, Promotion promotion) {
        return AddPromotionToInvoiceResponse.builder()
                .invoiceId(invoiceId)
                .promotionId(promotion.getId())
                .promotionName(promotion.getName())
                .promotionType(promotion.getType())
                .promotionValue(promotion.getValue())
                .promotionStartDate(promotion.getStartDate())
                .promotionEndDate(promotion.getEndDate())
                .promotionStatus(promotion.getStatus())
                .message("")
                .build();
    }
    private AddPromotionToInvoiceResponse convertToAddAddPromotionToInvoiceResponse(Long invoiceId, Promotion promotion, String message) {
        return AddPromotionToInvoiceResponse.builder()
                .invoiceId(invoiceId)
                .promotionId(promotion.getId())
                .promotionName(promotion.getName())
                .promotionType(promotion.getType())
                .promotionValue(promotion.getValue())
                .promotionStartDate(promotion.getStartDate())
                .promotionEndDate(promotion.getEndDate())
                .promotionStatus(promotion.getStatus())
                .message(message)
                .build();
    }

    private InvoiceDiscountResponse convertToInvoiceDiscount(InvoiceDiscount invoiceDiscount){
        return InvoiceDiscountResponse.builder()
                .discountId(invoiceDiscount.getDiscount() != null ? invoiceDiscount.getDiscount().getId() : null)
                .invoiceId(invoiceDiscount.getInvoice() != null ? invoiceDiscount.getInvoice().getId() : null)
                .appliedAmount(invoiceDiscount.getAppliedAmount())
                .message("")
                .build();
    }

    private InvoiceDiscountResponse convertToInvoiceDiscount(InvoiceDiscount invoiceDiscount, String message) {
        return InvoiceDiscountResponse.builder()
                .discountId(invoiceDiscount.getDiscount() != null ? invoiceDiscount.getDiscount().getId() : null)
                .invoiceId(invoiceDiscount.getInvoice() != null ? invoiceDiscount.getInvoice().getId() : null)
                .appliedAmount(invoiceDiscount.getAppliedAmount())
                .message(message)
                .build();
    }


    private InvoiceItemsResponse convertToInvoiceItemsResponse(
            Long invoiceId,
            List<Payment> paymentList,
            List<ProductInInvoice> productInInvoiceList,
            List<ServicePackageInInvoice> servicePackageInInvoiceList,
            List<ServiceInInvoice> serviceInInvoiceList,
            List<InvoiceDiscount> invoiceDiscountList,
            Prescription prescription,
            List<PrescriptionItem> prescriptionItemList,
            List<VaccineInInvoice> vaccineInInvoiceList,
            String message

    ){
        return InvoiceItemsResponse.builder()
                .invoiceId(invoiceId)
                .paymentList(
                    paymentList.stream().map(
                p -> PaymentResponse.builder()
                        .id(p.getId())
                        .paymentDate(p.getPaymentDate())
                        .amount(p.getAmount())
                        .method(p.getMethod())
                        .status(p.getStatus())
                        .statusDisplayName(p.getStatus().getDisplayName())
                        .transactionCode(p.getTransactionCode())
                        .invoiceId(invoiceId)
                        .description(p.getDescription())
                        .createdAt(p.getCreatedAt())
                        .updatedAt(p.getUpdatedAt())
                        .build()
                    ).toList()
                )
                .invoiceDiscountList(
                    invoiceDiscountList.stream().map(
                    i -> InvoiceDiscountResponse.builder()
                        .discountId(i.getDiscount() != null ? i.getDiscount().getId() : null)
                        .invoiceId(invoiceId)
                        .appliedAmount(i.getAppliedAmount())
                        .message("")
                        .build()
                    ).toList()
                )
                .serviceInInvoiceList(
                    serviceInInvoiceList.stream().map(
                    s -> ServiceInInvoiceResponse.builder()
                            .id(s.getId())
                            .price(s.getPrice())
                            .quantity(s.getQuantity())
                            .taxAmount(s.getTaxAmount())
                            .promotionAmount(s.getPromotionAmount())
                            .notes(s.getNotes())
                            .serviceId(s.getService() != null ? s.getService().getId() : null)
                            .serviceName(s.getService() != null ? s.getService().getName() : null)
                            .serviceDescription(s.getService() != null ? s.getService().getDescription() : null)
                            .serviceCategory(s.getService() != null ? s.getService().getCategory() : null)
                            .message("")
                            .build()
                    ).toList()
                )
                .servicePackageInInvoiceList(
                    servicePackageInInvoiceList.stream().map(
                sp -> ServicePackageInInvoiceResponse.builder()
                        .id(sp.getId())
                        .price(sp.getPrice())
                        .quantity(sp.getQuantity())
                        .taxAmount(sp.getTaxAmount())
                        .promotionAmount(sp.getPromotionAmount())
                        .notes(sp.getNotes())
                        .servicePackageId(sp.getServicePackage() != null ? sp.getServicePackage().getId() : null)
                        .servicePackageName(sp.getServicePackage() != null ? sp.getServicePackage().getName() : null)
                        .servicePackageDescription(sp.getServicePackage() != null ? sp.getServicePackage().getDescription() : null)
                        .message("")
                        .build()

                    ).toList()
                )
                .productInInvoiceList(
                        productInInvoiceList.stream().map(
                                pi -> ProductInInvoiceResponse.builder()
                                        .id(pi.getId())
                                        .price(pi.getPrice())
                                        .quantity(pi.getQuantity())
                                        .taxAmount(pi.getTaxAmount())
                                        .promotionAmount(pi.getPromotionAmount())
                                        .notes(pi.getNotes())
                                        .productId(pi.getProduct() != null ? pi.getProduct().getId() : null)
                                        .productName(pi.getProduct() != null ? pi.getProduct().getName() : null)
                                        .productCategory(pi.getProduct() != null ? pi.getProduct().getCategory() : null)
                                        .productBrand(pi.getProduct() != null ? pi.getProduct().getBrand() : null)
                                        .message("")
                                        .build()
                        ).toList()
                )
                .prescription(prescription != null ?
                        new PrescriptionInInvoiceResponse(
                        prescription.getId(),
                        prescription.getCreatedDate(),
                        prescription.getNotes(),
                        invoiceId,
                        prescription.getDoctor() != null ? prescription.getDoctor().getId() : null,
                        prescription.getDoctor() != null ? prescription.getDoctor().getFullname() : null,
                        ""
                )
                        : null)
                .prescriptionItemList(
                        prescriptionItemList.stream().map(
                                pri -> PrescriptionItemResponse.builder()
                                        .id(pri.getId())
                                        .dosage(pri.getDosage())
                                        .duration(pri.getDuration())
                                        .quantity(pri.getQuantity())
                                        .instruction(pri.getInstruction())
                                        .price(pri.getPrice())
                                        .taxPercent(pri.getTaxPercent())
                                        .promotionAmount(pri.getPromotionAmount())
                                        .totalAmount(pri.getPrice()*pri.getQuantity() + pri.getTaxAmount() - pri.getPromotionAmount())
                                        .medicineId(pri.getMedicine() != null ? pri.getMedicine().getId() : null)
                                        .medicineName(pri.getMedicine() != null ? pri.getMedicine().getName() : null)
                                        .medicineDescription(pri.getMedicine() != null ? pri.getMedicine().getDescription() : null)
                                        .prescriptionId(pri.getPrescription() != null ? pri.getPrescription().getId() : null)
                                        .message("")
                                        .build()
                        ).toList()
                )
                .vaccineInInvoiceList(
                        vaccineInInvoiceList.stream().map(
                                vi -> VaccineInInvoiceResponse.builder()
                                        .id(vi.getId())
                                        .price(vi.getPrice())
                                        .quantity(vi.getQuantity())
                                        .notes(vi.getNotes())
                                        .taxAmount(vi.getTaxAmount())
                                        .promotionAmount(vi.getPromotionAmount())
                                        .vaccineId(vi.getVaccine() != null ? vi.getVaccine().getId() : null)
                                        .vaccineName(vi.getVaccine() != null ? vi.getVaccine().getName() : null)
                                        .vaccineManufacturer(vi.getVaccine() != null ? vi.getVaccine().getManufacturer() : null)
                                        .build()
                        ).toList()
                )
                .build();
    }


    public InvoiceResponse confirmPaymentSuccess(Long invoiceId, ConfirmPaymentSuccessRequest request) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setStatus(InvoiceStatus.PAID);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        Payment paymentByInvoice = paymentRepository.findByInvoice(invoice).orElseThrow(() -> new RuntimeException("Payment not found"));
        paymentByInvoice.setStatus(PaymentStatus.SUCCESS);
        paymentByInvoice.setUpdatedAt(LocalDateTime.now());
        paymentByInvoice.setTransactionCode(request.getTransactionCode());
        Payment savedPayment = paymentRepository.save(paymentByInvoice);
        return convertToResponse(savedInvoice, "Payment for invoice with id " + savedInvoice.getId() + " success.");
    }

    public InvoiceResponse confirmPaymentFailed(Long invoiceId, ConfirmPaymentFailedRequest request) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setStatus(InvoiceStatus.VOID);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        Payment paymentByInvoice = paymentRepository.findByInvoice(savedInvoice).orElseThrow(() -> new RuntimeException("Payment not found"));

        paymentByInvoice.setStatus(request.getPaymentStatus());
        paymentByInvoice.setUpdatedAt(LocalDateTime.now());
        paymentByInvoice.setTransactionCode(request.getTransactionCode());

        Payment savedPayment = paymentRepository.save(paymentByInvoice);
        return convertToResponse(savedInvoice, "Payment for invoice with id " + savedInvoice.getId() + " failed.");
    }
}


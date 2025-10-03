package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfomationInvoiceResponse {
    private Long id;
    private LocalDate createdDate;
    private Double totalAmount;
    private String status;
    private Double totalDiscountAmount;
    private Double taxTotalAmount;
    private Double finalAmount;

    // Staff information
    private Long staffId;
    private String staffName;
    private String staffPosition;

    // Customer information
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;

    // Invoice items
    private List<ProductInInvoiceResponse> products;
    private List<ServiceInInvoiceResponse> services;
    private List<ServicePackageInInvoiceResponse> servicePackages;
    private List<VaccineInInvoiceResponse> vaccines;
    private List<InvoiceDiscountResponse> discounts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductInInvoiceResponse {
        private Long id;
        private Double price;
        private Integer quantity;
        private String notes;
        private Long productId;
        private String productName;
        private String productCategory;
        private String productBrand;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServiceInInvoiceResponse {
        private Long id;
        private Double price;
        private Integer quantity;
        private String notes;
        private Long serviceId;
        private String serviceName;
        private String serviceCategory;
        private Integer serviceDuration;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServicePackageInInvoiceResponse {
        private Long id;
        private Double price;
        private Integer quantity;
        private String notes;
        private Long servicePackageId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VaccineInInvoiceResponse {
        private Long id;
        private Double price;
        private Integer quantity;
        private String notes;
        private Long vaccineId;
        private String vaccineName;
        private String vaccineManufacturer;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InvoiceDiscountResponse {
        private Long id;
        private Double appliedAmount;
        private Long discountId;
        private String discountCode;
        private String discountDescription;
        private Double discountValue;
    }
}

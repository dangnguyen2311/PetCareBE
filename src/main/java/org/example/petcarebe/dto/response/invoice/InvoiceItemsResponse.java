package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.dto.response.payment.PaymentResponse;
import org.example.petcarebe.dto.response.prescription.PrescriptionItemResponse;
import org.example.petcarebe.dto.response.prescription.PrescriptionResponse;
import org.example.petcarebe.dto.response.vaccine.VaccineInInvoiceResponse;
import org.example.petcarebe.model.Prescription;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceItemsResponse {
    private Long invoiceId;
    private PrescriptionInInvoiceResponse prescription;
    private List<VaccineInInvoiceResponse> vaccineInInvoiceList;
    private List<PrescriptionItemResponse> prescriptionItemList;
    private List<ServiceInInvoiceResponse> serviceInInvoiceList;
    private List<InvoiceDiscountResponse> invoiceDiscountList;
    private List<ServicePackageInInvoiceResponse> servicePackageInInvoiceList;
    private List<ProductInInvoiceResponse> productInInvoiceList;
    private List<PaymentResponse> paymentList;
    private String message;
}

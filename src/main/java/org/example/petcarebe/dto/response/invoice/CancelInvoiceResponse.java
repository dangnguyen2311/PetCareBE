package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.dto.response.discount.DiscountResponse;
import org.example.petcarebe.dto.response.prescription.PrescriptionResponse;
import org.example.petcarebe.dto.response.product.ProductResponse;
import org.example.petcarebe.dto.response.service.ServiceResponse;
import org.example.petcarebe.dto.response.servicepackage.ServicePackageResponse;
import org.example.petcarebe.dto.response.vaccine.VaccineResponse;
import org.example.petcarebe.enums.InvoiceStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelInvoiceResponse {
    private Long id;
    private String customerName;
    private String customerPhone;
    private String staffName;
    private InvoiceStatus status;

//    private List<PrescriptionResponse> prescriptionList;
//    private List<ServiceResponse> serviceList;
//    private List<ServicePackageResponse> servicePackageList;
//    private List<ProductResponse> productList;
//    private List<VaccineResponse> vaccineList;
//    private List<DiscountResponse>  discountList;

    private String message;

}

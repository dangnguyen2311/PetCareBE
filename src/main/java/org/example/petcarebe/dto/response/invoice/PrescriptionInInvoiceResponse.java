package org.example.petcarebe.dto.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.petcarebe.dto.response.prescription.PrescriptionItemResponse;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionInInvoiceResponse {
    private Long id;
    private LocalDate createdDate;
    private String note;
    private Long invoiceId;
    private Long doctorId;
    private String doctorName;
    private String message;
}

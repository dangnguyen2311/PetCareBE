package org.example.petcarebe.dto.response.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionResponse {
    private Long id;
    private LocalDate createdDate;
    private String note;
    private Long invoiceId;
    private Long doctorId;
    private String doctorName;
    private List<PrescriptionItemResponse> items;
    private Double totalAmount; // Sum of all prescription items
    private Integer itemCount; // Number of items in prescription
}

package org.example.petcarebe.dto.request.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddServicePackageToInvoiceRequest {
    private Long servicePackageId;
    private Integer quantity;
    private Double taxPercent;
    private String notes;
}

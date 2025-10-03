package org.example.petcarebe.dto.request.visit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitDateRangeRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}

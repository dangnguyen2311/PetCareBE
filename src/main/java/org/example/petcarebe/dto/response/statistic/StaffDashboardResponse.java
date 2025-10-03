package org.example.petcarebe.dto.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffDashboardResponse {
    private Integer appointmentTodayNums;
    private Integer appointmentNeedApprovedTodayNums;
    private Integer prescriptionTodayNums;
    private Integer invoicedTodayNums;
    private Integer paymentTodayNums;
    private Integer serviceAndServicePackageUsedTodayNums;
    private Integer productUsedTodayNums;
    private Integer discountUsesTodayNums;
    private Integer promotionActiveNums;
    private String message;
}

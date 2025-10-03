package org.example.petcarebe.dto.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDashboardResponse {
    private Integer unfinishedTestTodayNums;
    private Integer appointmentTodayNums;
    private Integer petsTodayNeedExamNums;
    private Integer vaccineToBeAdministeredNums;
    private Integer visitNums;
    private Integer prescriptionTodayNums;
    private String message;
}

package org.example.petcarebe.dto.response.staff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffStatisticsResponse {
    // Staff basic information
    private Long staffId;
    private String staffName;
    private String position;
    
    // Invoice statistics
    private Integer totalInvoicesProcessed;
    private Double totalRevenueProcessed;
    private Double averageInvoiceAmount;
    private Integer invoicesThisMonth;
    private Integer invoicesThisWeek;
    private Integer invoicesToday;
    
    // Monthly breakdown
    private List<MonthlyInvoiceInfo> monthlyBreakdown;
    
    // Performance statistics
    private Double monthlyRevenueTarget; // TODO: Can be configured
    private Double monthlyRevenueActual;
    private Double performanceRate; // Actual/Target * 100
    
    // Time-based statistics
    private String firstInvoiceDate;
    private String lastInvoiceDate;
    private String mostProductiveMonth;
    private String leastProductiveMonth;
    
    // Customer service statistics
    private Integer totalCustomersServed;
    private Integer uniqueCustomersServed;
    private Double averageCustomersPerDay;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyInvoiceInfo {
        private String month;
        private Integer invoiceCount;
        private Double totalRevenue;
        private Integer customersServed;
        private Double averageInvoiceAmount;
    }
}

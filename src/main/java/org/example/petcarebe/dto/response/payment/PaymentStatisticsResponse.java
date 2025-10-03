package org.example.petcarebe.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentStatisticsResponse {
    
    // Overall statistics
    private Long totalPayments;
    private Double totalAmount;
    private Double totalRefunded;
    private Double netRevenue; // totalAmount - totalRefunded
    
    // Status breakdown
    private Long successfulPayments;
    private Long pendingPayments;
    private Long failedPayments;
    private Long refundedPayments;
    
    // Amount breakdown by status
    private Double successfulAmount;
    private Double pendingAmount;
    private Double failedAmount;
    
    // Method breakdown
    private Map<String, Long> paymentsByMethod;
    private Map<String, Double> amountByMethod;
    
    // Date range
    private LocalDate fromDate;
    private LocalDate toDate;
    
    // Daily statistics (for charts)
    private Map<LocalDate, Double> dailyRevenue;
    private Map<LocalDate, Long> dailyTransactions;
}

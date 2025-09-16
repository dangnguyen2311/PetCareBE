package org.example.petcarebe.dto.response.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerStatisticsResponse {
    // Customer basic info
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;

    // Financial statistics
    private Double totalSpent;
    private Double averageSpentPerVisit;
    private Integer totalInvoices;
    private Integer paidInvoices;
    private Integer unpaidInvoices;

    // Appointment statistics
    private Integer totalAppointments;
    private Integer completedAppointments;
    private Integer cancelledAppointments;
    private Integer pendingAppointments;

    // Pet statistics
    private Integer totalPets;

    // Service usage statistics
    private Integer totalServicesUsed;
    private Integer totalProductsPurchased;
    private Integer totalVaccinesReceived;

    // Time-based statistics
    private String memberSince; // How long they've been a customer
    private String lastVisitDate;
    private String nextAppointmentDate;
}

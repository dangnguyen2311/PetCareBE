package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.servicepackageprice.CreateServicePackagePriceHistoryRequest;
import org.example.petcarebe.dto.request.serviceprice.CreateServicePriceHistoryRequest;
import org.example.petcarebe.dto.response.servicepackageprice.ServicePackagePriceHistoryResponse;
import org.example.petcarebe.dto.response.serviceprice.ServicePriceHistoryResponse;
import org.example.petcarebe.model.Service;
import org.example.petcarebe.model.ServicePriceHistory;
import org.example.petcarebe.repository.ServicePriceHistoryRepository;
import org.example.petcarebe.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@org.springframework.stereotype.Service
public class ServicePriceHistoryService {
    @Autowired
    private ServicePriceHistoryRepository servicePriceHistoryRepository;
    @Autowired
    private ServiceRepository  serviceRepository;

    public ServicePriceHistoryResponse createPriceHistory(CreateServicePriceHistoryRequest request) {
        servicePriceHistoryRepository.deactiveAllActiveRecord(LocalDate.now());

        ServicePriceHistory servicePriceHistory = new ServicePriceHistory();
        Service service = serviceRepository.findById(request.getServiceId()).orElseThrow(() -> new RuntimeException("service not found"));
        servicePriceHistory.setService(service);
        servicePriceHistory.setPrice(request.getPrice());
        servicePriceHistory.setEndDate(LocalDate.now());
        servicePriceHistory.setStartDate(LocalDate.now());
        servicePriceHistory.setStatus("ACTIVE");
        ServicePriceHistory savedServicePriceHistory = servicePriceHistoryRepository.save(servicePriceHistory);

        return convertToResponse(savedServicePriceHistory);

    }

    private ServicePriceHistoryResponse convertToResponse(ServicePriceHistory servicePriceHistory) {
        return ServicePriceHistoryResponse.builder()
                .id(servicePriceHistory.getId())
                .serviceId(servicePriceHistory.getService().getId())
                .serviceName(servicePriceHistory.getService().getName())
                .serviceDescription(servicePriceHistory.getService().getDescription())
                .price(servicePriceHistory.getPrice())
                .startDate(servicePriceHistory.getStartDate())
                .endDate(servicePriceHistory.getEndDate())
                .status(servicePriceHistory.getStatus())
                .message("")
                .build();
    }
    private ServicePriceHistoryResponse convertToResponse(ServicePriceHistory servicePriceHistory, String message) {
        return ServicePriceHistoryResponse.builder()
                .id(servicePriceHistory.getId())
                .serviceId(servicePriceHistory.getService().getId())
                .serviceName(servicePriceHistory.getService().getName())
                .serviceDescription(servicePriceHistory.getService().getDescription())
                .price(servicePriceHistory.getPrice())
                .startDate(servicePriceHistory.getStartDate())
                .endDate(servicePriceHistory.getEndDate())
                .status(servicePriceHistory.getStatus())
                .message(message)
                .build();
    }
}

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
import java.util.ArrayList;
import java.util.List;

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

    public List<ServicePriceHistoryResponse> getAllServicePriceHistory() {
        List<ServicePriceHistory> responses = servicePriceHistoryRepository.findAll();
        return responses.stream().map(this::convertToResponse).toList();
    }

    private ServicePriceHistoryResponse convertToResponse(ServicePriceHistory servicePriceHistory) {
        return ServicePriceHistoryResponse.builder()
                .id(servicePriceHistory.getId())
                .serviceId(servicePriceHistory.getService() != null ? servicePriceHistory.getService().getId() : null)
                .serviceName(servicePriceHistory.getService() != null ? servicePriceHistory.getService().getName() : null)
                .serviceDescription(servicePriceHistory.getService() != null ? servicePriceHistory.getService().getDescription() : null)
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
                .serviceId(servicePriceHistory.getService() != null ? servicePriceHistory.getService().getId() : null)
                .serviceName(servicePriceHistory.getService() != null ? servicePriceHistory.getService().getName() : null)
                .serviceDescription(servicePriceHistory.getService() != null ? servicePriceHistory.getService().getDescription() : null)
                .price(servicePriceHistory.getPrice())
                .startDate(servicePriceHistory.getStartDate())
                .endDate(servicePriceHistory.getEndDate())
                .status(servicePriceHistory.getStatus())
                .message(message)
                .build();
    }


}

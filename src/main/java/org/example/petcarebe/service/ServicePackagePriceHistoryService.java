package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.servicepackageprice.CreateServicePackagePriceHistoryRequest;
import org.example.petcarebe.dto.response.servicepackageprice.ServicePackagePriceHistoryResponse;
import org.example.petcarebe.model.ServicePackage;
import org.example.petcarebe.model.ServicePackagePriceHistory;
import org.example.petcarebe.repository.ServicePackagePriceHistoryRepository;
import org.example.petcarebe.repository.ServicePackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServicePackagePriceHistoryService {
    @Autowired
    private ServicePackagePriceHistoryRepository  servicePackagePriceHistoryRepository;

    @Autowired
    private ServicePackageRepository servicePackageRepository;


    public ServicePackagePriceHistoryResponse createPriceHistory(CreateServicePackagePriceHistoryRequest request) {
        servicePackagePriceHistoryRepository.deactiveAllActiveRecord(LocalDate.now());

        ServicePackagePriceHistory servicePackagePriceHistory = new ServicePackagePriceHistory();
        ServicePackage servicePackage = servicePackageRepository.findById(request.getServicePackageId()).orElseThrow(() -> new RuntimeException("Service Package Not Found"));
        servicePackagePriceHistory.setServicePackage(servicePackage);
        servicePackagePriceHistory.setPrice(request.getPrice());
        servicePackagePriceHistory.setStartDate(LocalDate.now());
        servicePackagePriceHistory.setEndDate(null);
        servicePackagePriceHistory.setStatus("ACTIVE");

        ServicePackagePriceHistory savedServicePackagePriceHistory = servicePackagePriceHistoryRepository.save(servicePackagePriceHistory);
        return convertToResponse(savedServicePackagePriceHistory);

    }
    public List<ServicePackagePriceHistoryResponse> getAllServicePackagePriceHistories() {
        List<ServicePackagePriceHistory> responses = servicePackagePriceHistoryRepository.findAll();
        return responses.stream().map(this::convertToResponse).toList();
    }
    private ServicePackagePriceHistoryResponse convertToResponse(ServicePackagePriceHistory servicePackagePriceHistory) {
        return ServicePackagePriceHistoryResponse.builder()
                .id(servicePackagePriceHistory.getId())
                .servicePackageId(servicePackagePriceHistory.getServicePackage() != null ? servicePackagePriceHistory.getServicePackage().getId() : null)
                .servicePackageName(servicePackagePriceHistory.getServicePackage() != null ? servicePackagePriceHistory.getServicePackage().getName() : null)
                .servicePackageDescription(servicePackagePriceHistory.getServicePackage() != null ? servicePackagePriceHistory.getServicePackage().getDescription() : null)
                .startDate(servicePackagePriceHistory.getStartDate())
                .endDate(servicePackagePriceHistory.getEndDate())
                .status(servicePackagePriceHistory.getStatus())
                .price(servicePackagePriceHistory.getPrice())
                .message("")
                .build();
    }
    private ServicePackagePriceHistoryResponse convertToResponse(ServicePackagePriceHistory servicePackagePriceHistory, String message) {
        return ServicePackagePriceHistoryResponse.builder()
                .id(servicePackagePriceHistory.getId())
                .servicePackageId(servicePackagePriceHistory.getServicePackage() != null ? servicePackagePriceHistory.getServicePackage().getId() : null)
                .servicePackageName(servicePackagePriceHistory.getServicePackage() != null ? servicePackagePriceHistory.getServicePackage().getName() : null)
                .servicePackageDescription(servicePackagePriceHistory.getServicePackage() != null ? servicePackagePriceHistory.getServicePackage().getDescription() : null)
                .startDate(servicePackagePriceHistory.getStartDate())
                .endDate(servicePackagePriceHistory.getEndDate())
                .status(servicePackagePriceHistory.getStatus())
                .price(servicePackagePriceHistory.getPrice())
                .message(message)
                .build();
    }


}

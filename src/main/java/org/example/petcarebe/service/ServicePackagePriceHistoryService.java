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
    private ServicePackagePriceHistoryResponse convertToResponse(ServicePackagePriceHistory servicePackagePriceHistory) {
        return ServicePackagePriceHistoryResponse.builder()
                .id(servicePackagePriceHistory.getId())
                .servicePackageId(servicePackagePriceHistory.getServicePackage().getId())
                .servicePackageName(servicePackagePriceHistory.getServicePackage().getName())
                .servicePackageDescription(servicePackagePriceHistory.getServicePackage().getDescription())
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
                .servicePackageId(servicePackagePriceHistory.getServicePackage().getId())
                .servicePackageName(servicePackagePriceHistory.getServicePackage().getName())
                .servicePackageDescription(servicePackagePriceHistory.getServicePackage().getDescription())
                .startDate(servicePackagePriceHistory.getStartDate())
                .endDate(servicePackagePriceHistory.getEndDate())
                .status(servicePackagePriceHistory.getStatus())
                .price(servicePackagePriceHistory.getPrice())
                .message(message)
                .build();
    }
}

package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.servicepackage.CreateServicePackageRequest;
import org.example.petcarebe.dto.request.servicepackage.UpdateServicePackageRequest;
import org.example.petcarebe.dto.response.servicepackage.ServicePackageResponse;
import org.example.petcarebe.model.ServicePackage;
import org.example.petcarebe.repository.ServicePackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServicePackageService {

    @Autowired
    private ServicePackageRepository servicePackageRepository;

    public ServicePackageResponse createServicePackage(CreateServicePackageRequest request) {
        ServicePackage servicePackage = new ServicePackage();
        servicePackage.setName(request.getName());
        servicePackage.setDescription(request.getDescription());
        servicePackage.setImgUrl(request.getImgUrl());
        servicePackage.setStatus(request.getStatus());
        servicePackage.setDuration(request.getDuration());
        servicePackage.setCreatedDate(LocalDate.now());
        servicePackage.setIsDeleted(false);

        ServicePackage savedServicePackage = servicePackageRepository.save(servicePackage);
        return convertToResponse(savedServicePackage);
    }

    public List<ServicePackageResponse> getAllServicePackages() {
        return servicePackageRepository.findAllByIsDeleted(false).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ServicePackageResponse getServicePackageById(Long id) {
        ServicePackage servicePackage = servicePackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServicePackage not found with id: " + id));
        return convertToResponse(servicePackage);
    }

    public ServicePackageResponse updateServicePackage(Long id, UpdateServicePackageRequest request) {
        ServicePackage servicePackage = servicePackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServicePackage not found with id: " + id));

        servicePackage.setName(request.getName());
        servicePackage.setDescription(request.getDescription());
        servicePackage.setImgUrl(request.getImgUrl());
        servicePackage.setStatus(request.getStatus());
        servicePackage.setDuration(request.getDuration());
        servicePackage.setUpdatedAt(LocalDate.now());

        ServicePackage updatedServicePackage = servicePackageRepository.save(servicePackage);
        return convertToResponse(updatedServicePackage);
    }

    public void deleteServicePackage(Long id) {
        ServicePackage servicePackage = servicePackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServicePackage not found with id: " + id));
        servicePackage.setIsDeleted(true);
        servicePackageRepository.save(servicePackage);
    }

    private ServicePackageResponse convertToResponse(ServicePackage servicePackage) {
        return new ServicePackageResponse(
                servicePackage.getId(),
                servicePackage.getName(),
                servicePackage.getDescription(),
                servicePackage.getImgUrl(),
                servicePackage.getStatus(),
                servicePackage.getDuration(),
                servicePackage.getCreatedDate(),
                servicePackage.getUpdatedAt()
        );
    }
}


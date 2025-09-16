package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.service.CreateServiceRequest;
import org.example.petcarebe.dto.request.service.UpdateServiceRequest;
import org.example.petcarebe.dto.response.service.ServiceResponse;
import org.example.petcarebe.model.Service;
import org.example.petcarebe.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public ServiceResponse createService(CreateServiceRequest request) {
        Service service = new Service();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setImgUrl(request.getImgUrl());
        service.setStatus(request.getStatus());
        service.setDuration(request.getDuration());
        service.setCategory(request.getCategory());
        service.setCreatedDate(LocalDate.now());
        service.setIsDeleted(false);

        Service savedService = serviceRepository.save(service);
        return convertToResponse(savedService);
    }

    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ServiceResponse getServiceById(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        return convertToResponse(service);
    }

    public ServiceResponse updateService(Long id, UpdateServiceRequest request) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));

        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setImgUrl(request.getImgUrl());
        service.setStatus(request.getStatus());
        service.setDuration(request.getDuration());
        service.setCategory(request.getCategory());
        service.setUpdatedAt(LocalDate.now());

        Service updatedService = serviceRepository.save(service);
        return convertToResponse(updatedService);
    }

    public void deleteService(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        service.setIsDeleted(true);
        serviceRepository.save(service);
    }

    private ServiceResponse convertToResponse(Service service) {
        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getImgUrl(),
                service.getStatus(),
                service.getDuration(),
                service.getCategory(),
                service.getCreatedDate(),
                service.getUpdatedAt()
        );
    }
}


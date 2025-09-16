package org.example.petcarebe.controller;

import org.example.petcarebe.dto.request.servicepackage.CreateServicePackageRequest;
import org.example.petcarebe.dto.request.servicepackage.UpdateServicePackageRequest;
import org.example.petcarebe.dto.response.servicepackage.ServicePackageResponse;
import org.example.petcarebe.service.ServicePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/service-packages")
public class ServicePackageController {

    @Autowired
    private ServicePackageService servicePackageService;

    @PostMapping
    public ResponseEntity<ServicePackageResponse> createServicePackage(@RequestBody CreateServicePackageRequest request) {
        ServicePackageResponse createdServicePackage = servicePackageService.createServicePackage(request);
        return ResponseEntity.ok(createdServicePackage);
    }

    @GetMapping
    public ResponseEntity<List<ServicePackageResponse>> getAllServicePackages() {
        List<ServicePackageResponse> servicePackages = servicePackageService.getAllServicePackages();
        return ResponseEntity.ok(servicePackages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicePackageResponse> getServicePackageById(@PathVariable Long id) {
        ServicePackageResponse servicePackage = servicePackageService.getServicePackageById(id);
        return ResponseEntity.ok(servicePackage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicePackageResponse> updateServicePackage(@PathVariable Long id, @RequestBody UpdateServicePackageRequest request) {
        ServicePackageResponse updatedServicePackage = servicePackageService.updateServicePackage(id, request);
        return ResponseEntity.ok(updatedServicePackage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicePackage(@PathVariable Long id) {
        servicePackageService.deleteServicePackage(id);
        return ResponseEntity.noContent().build();
    }
}


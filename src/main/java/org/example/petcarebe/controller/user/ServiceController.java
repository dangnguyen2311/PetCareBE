package org.example.petcarebe.controller.user;

import org.example.petcarebe.dto.request.service.CreateServiceRequest;
import org.example.petcarebe.dto.request.service.UpdateServiceRequest;
import org.example.petcarebe.dto.response.service.ServiceResponse;
import org.example.petcarebe.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @PostMapping
    public ResponseEntity<ServiceResponse> createService(@RequestBody CreateServiceRequest request) {
        ServiceResponse createdService = serviceService.createService(request);
        return ResponseEntity.ok(createdService);
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        List<ServiceResponse> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        ServiceResponse service = serviceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> updateService(@PathVariable Long id, @RequestBody UpdateServiceRequest request) {
        ServiceResponse updatedService = serviceService.updateService(id, request);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
//        return ResponseEntity.noContent().build();
        return ResponseEntity.ok().build();
    }
}


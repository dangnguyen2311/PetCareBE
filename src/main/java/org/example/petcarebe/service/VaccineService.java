package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.vaccine.CreateVaccineRequest;
import org.example.petcarebe.dto.request.vaccine.UpdateVaccineRequest;
import org.example.petcarebe.dto.response.vaccine.VaccineResponse;
import org.example.petcarebe.model.Vaccine;
import org.example.petcarebe.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VaccineService {

    @Autowired
    private VaccineRepository vaccineRepository;

    public VaccineResponse createVaccine(CreateVaccineRequest request) {
        Vaccine vaccine = new Vaccine();
        vaccine.setName(request.getName());
        vaccine.setManufacturer(request.getManufacturer());
        vaccine.setDescription(request.getDescription());
        vaccine.setIsDeleted(false);

        Vaccine savedVaccine = vaccineRepository.save(vaccine);
        return convertToResponse(savedVaccine);
    }

    public List<VaccineResponse> getAllVaccines() {
        return vaccineRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public VaccineResponse getVaccineById(Long id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id: " + id));
        return convertToResponse(vaccine);
    }

    public VaccineResponse updateVaccine(Long id, UpdateVaccineRequest request) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id: " + id));

        vaccine.setName(request.getName());
        vaccine.setManufacturer(request.getManufacturer());
        vaccine.setDescription(request.getDescription());

        Vaccine updatedVaccine = vaccineRepository.save(vaccine);
        return convertToResponse(updatedVaccine);
    }

    public void deleteVaccine(Long id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id: " + id));
        vaccine.setIsDeleted(true);
        vaccineRepository.save(vaccine);
    }

    private VaccineResponse convertToResponse(Vaccine vaccine) {
        return new VaccineResponse(
                vaccine.getId(),
                vaccine.getName(),
                vaccine.getManufacturer(),
                vaccine.getDescription()
        );
    }
}


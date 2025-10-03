package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.vaccineprice.CreateVaccinePriceHistoryRequest;
import org.example.petcarebe.dto.response.vaccineprice.VaccinePriceHistoryResponse;
import org.example.petcarebe.model.Vaccine;
import org.example.petcarebe.model.VaccinePriceHistory;
import org.example.petcarebe.repository.VaccinePriceHistoryRepository;
import org.example.petcarebe.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VaccinePriceHistoryService {
    @Autowired
    private VaccinePriceHistoryRepository vaccinePriceHistoryRepository;
    @Autowired
    private VaccineRepository vaccineRepository;


    public VaccinePriceHistoryResponse createPriceHistory(CreateVaccinePriceHistoryRequest request) {
        vaccinePriceHistoryRepository.deactiveAllActiveRecord(LocalDate.now());

        VaccinePriceHistory vaccinePriceHistory = new VaccinePriceHistory();
        Vaccine vaccine = vaccineRepository.findById(request.getVaccineId()).orElseThrow(() -> new RuntimeException("Vaccine not found"));
        vaccinePriceHistory.setVaccine(vaccine);
        vaccinePriceHistory.setPrice(request.getPrice());
        vaccinePriceHistory.setStartDate(LocalDate.now());
        vaccinePriceHistory.setEndDate(null);
        vaccinePriceHistory.setStatus("ACTIVE");

        VaccinePriceHistory savedVaccinePriceHistory = vaccinePriceHistoryRepository.save(vaccinePriceHistory);
        return convertToResponse(savedVaccinePriceHistory);
    }

    public List<VaccinePriceHistoryResponse> getAllVaccinePriceHistory() {
        List<VaccinePriceHistory> responses = vaccinePriceHistoryRepository.findAll();
        return responses.stream().map(this::convertToResponse).toList();
    }

    private VaccinePriceHistoryResponse convertToResponse(VaccinePriceHistory vaccinePriceHistory) {
        return VaccinePriceHistoryResponse.builder()
                .id(vaccinePriceHistory.getId())
                .vaccineId(vaccinePriceHistory.getVaccine() != null ?  vaccinePriceHistory.getVaccine().getId() : null)
                .vaccineName(vaccinePriceHistory.getVaccine() != null ? vaccinePriceHistory.getVaccine().getName() : null)
                .vaccineManufacturer(vaccinePriceHistory.getVaccine() != null ? vaccinePriceHistory.getVaccine().getManufacturer() : null)
                .price(vaccinePriceHistory.getPrice())
                .startDate(vaccinePriceHistory.getStartDate())
                .endDate(vaccinePriceHistory.getEndDate())
                .status(vaccinePriceHistory.getStatus())
                .message("")
                .build();
    }
    private VaccinePriceHistoryResponse convertToResponse(VaccinePriceHistory vaccinePriceHistory, String message) {
        return VaccinePriceHistoryResponse.builder()
                .id(vaccinePriceHistory.getId())
                .vaccineId(vaccinePriceHistory.getVaccine() != null ?  vaccinePriceHistory.getVaccine().getId() : null)
                .vaccineName(vaccinePriceHistory.getVaccine() != null ? vaccinePriceHistory.getVaccine().getName() : null)
                .vaccineManufacturer(vaccinePriceHistory.getVaccine() != null ? vaccinePriceHistory.getVaccine().getManufacturer() : null)
                .vaccineManufacturer(vaccinePriceHistory.getVaccine().getManufacturer())
                .price(vaccinePriceHistory.getPrice())
                .startDate(vaccinePriceHistory.getStartDate())
                .endDate(vaccinePriceHistory.getEndDate())
                .status(vaccinePriceHistory.getStatus())
                .message(message)
                .build();
    }


}

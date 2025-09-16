package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.medicineprice.CreateMedicinePriceRequest;
import org.example.petcarebe.dto.response.medicineprice.MedicinePriceHistoryResponse;
import org.example.petcarebe.model.Medicine;
import org.example.petcarebe.model.MedicinePriceHistory;
import org.example.petcarebe.repository.MedicinePriceHistoryRepository;
import org.example.petcarebe.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MedicinePriceHistoryService {
    @Autowired
    private MedicinePriceHistoryRepository medicinePriceHistoryRepository;

    @Autowired
    private MedicineRepository  medicineRepository;


    public MedicinePriceHistoryResponse createPriceHistory(CreateMedicinePriceRequest request) {
        medicinePriceHistoryRepository.deactiveAllActiveRecord(LocalDate.now());
//        for(MedicinePriceHistory medicinePriceHistory : lastMedicinePriceHistoryList) {
//            medicinePriceHistory.setStatus("INACTIVE");
//            if(medicinePriceHistory.getEndDate() == null) {
//                medicinePriceHistory.setEndDate(LocalDate.now());
//            }
//            medicinePriceHistoryRepository.save(medicinePriceHistory);
//        }
        MedicinePriceHistory medicinePriceHistory = new MedicinePriceHistory();
        Optional<Medicine> medicine = medicineRepository.findById(request.getMedicineId());
        medicinePriceHistory.setMedicine(medicine.orElseThrow(() -> new RuntimeException("Medicine not found!")));
        medicinePriceHistory.setStartDate(LocalDate.now());
        medicinePriceHistory.setEndDate(null);
        medicinePriceHistory.setPrice(request.getPrice());
        medicinePriceHistory.setStatus("ACTIVE");
        MedicinePriceHistory saveMedicinePriceHistory = medicinePriceHistoryRepository.save(medicinePriceHistory);

        return convertToResponse(saveMedicinePriceHistory);
    }

    private MedicinePriceHistoryResponse convertToResponse(MedicinePriceHistory medicinePriceHistory) {
        return MedicinePriceHistoryResponse.builder()
                .id(medicinePriceHistory.getId())
                .Medicineid(medicinePriceHistory.getMedicine().getId())
                .MedicineName(medicinePriceHistory.getMedicine().getName())
                .price(medicinePriceHistory.getPrice())
                .startDate(medicinePriceHistory.getStartDate())
                .endDate(medicinePriceHistory.getEndDate())
                .status(medicinePriceHistory.getStatus())
                .message("")
                .build();
    }

    private MedicinePriceHistoryResponse convertToResponse(MedicinePriceHistory medicinePriceHistory,String message) {
        return MedicinePriceHistoryResponse.builder()
                .id(medicinePriceHistory.getId())
                .Medicineid(medicinePriceHistory.getMedicine().getId())
                .MedicineName(medicinePriceHistory.getMedicine().getName())
                .price(medicinePriceHistory.getPrice())
                .startDate(medicinePriceHistory.getStartDate())
                .endDate(medicinePriceHistory.getEndDate())
                .status(medicinePriceHistory.getStatus())
                .message(message)
                .build();
    }
}

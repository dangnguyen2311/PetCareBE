package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.medicine.CreateMedicineRequest;
import org.example.petcarebe.dto.request.medicine.UpdateMedicineRequest;
import org.example.petcarebe.dto.response.medicine.MedicineResponse;
import org.example.petcarebe.enums.InventoryObjectType;
import org.example.petcarebe.model.InventoryObject;
import org.example.petcarebe.model.Medicine;
import org.example.petcarebe.repository.InventoryObjectRepository;
import org.example.petcarebe.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private InventoryObjectRepository inventoryObjectRepository;

    public MedicineResponse createMedicine(CreateMedicineRequest request) {
        // tạo thuốc kèm tạo object trong kho InventoryObject, sau đó tạo InventoryItem sau
        InventoryObject  inventoryObject = new InventoryObject();
        inventoryObject.setName(request.getName());
        inventoryObject.setType(InventoryObjectType.MEDICINE);
        inventoryObject.setDescription(request.getDescription());
        InventoryObject savedInventoryObject = inventoryObjectRepository.save(inventoryObject);

        Medicine medicine = new Medicine();
        medicine.setName(request.getName());
        medicine.setDescription(request.getDescription());
        medicine.setUnit(request.getUnit());
        medicine.setNotes(request.getNotes());
        medicine.setIsDeleted(false);
        medicine.setInventoryObject(savedInventoryObject);

        Medicine savedMedicine = medicineRepository.save(medicine);
        return convertToResponse(savedMedicine);
    }

    public List<MedicineResponse> getAllMedicines() {
        return medicineRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public MedicineResponse getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        return convertToResponse(medicine);
    }

    public MedicineResponse updateMedicine(Long id, UpdateMedicineRequest request) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));

        medicine.setName(request.getName());
        medicine.setDescription(request.getDescription());
        medicine.setUnit(request.getUnit());
        medicine.setNotes(request.getNotes());

        Medicine updatedMedicine = medicineRepository.save(medicine);
        return convertToResponse(updatedMedicine);
    }

    public void deleteMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        medicine.setIsDeleted(true);
        medicineRepository.save(medicine);
    }

    private MedicineResponse convertToResponse(Medicine medicine) {
        return new MedicineResponse(
                medicine.getId(),
                medicine.getName(),
                medicine.getDescription(),
                medicine.getUnit(),
                medicine.getNotes()
        );
    }
}


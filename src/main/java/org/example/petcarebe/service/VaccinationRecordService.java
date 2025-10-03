package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.vaccinationrecord.CreateVaccinationRecordRequest;
import org.example.petcarebe.dto.response.vaccinationrecord.VaccinationRecordResponse;
import org.example.petcarebe.model.*;
import org.example.petcarebe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VaccinationRecordService {
    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;

    @Autowired
    private VaccineScheduleRepository vaccineScheduleRepository;

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private VisitRepository visitRepository;

    //STATUS = UNVACCINATED, VACCINATED

    public VaccinationRecordResponse createVaccinationRecord(CreateVaccinationRecordRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor Not Found"));
        Vaccine vaccine = vaccineRepository.findById(request.getVaccineId())
                .orElseThrow(() -> new IllegalArgumentException("Vaccine Not Found"));
        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new IllegalArgumentException("Pet Not Found"));
        Visit visit = visitRepository.findById(request.getVisitId())
                .orElse(null);

        VaccinationRecord vaccinationRecord = new VaccinationRecord();
        vaccinationRecord.setDoctor(doctor);
        vaccinationRecord.setVaccine(vaccine);
        vaccinationRecord.setVisit(visit);
        vaccinationRecord.setPet(pet);
        vaccinationRecord.setVaccinationDate(request.getVaccinationDate());
        vaccinationRecord.setNextDueDate(request.getNextDueDate());
        vaccinationRecord.setNotes(request.getNotes());
        vaccinationRecord.setStatus("UNVACCINATED");

        VaccinationRecord savedVaccinationRecord = vaccinationRecordRepository.save(vaccinationRecord);
        return convertToResponse(savedVaccinationRecord, "Vaccination Record created successfully");
    }

    private VaccinationRecordResponse convertToResponse(VaccinationRecord vaccinationRecord){
        return VaccinationRecordResponse.builder()
                .vaccinationRecordId(vaccinationRecord.getId())
                .vaccinationDate(vaccinationRecord.getVaccinationDate())
                .nextDueDate(vaccinationRecord.getNextDueDate())
                .status(vaccinationRecord.getStatus())
                .notes(vaccinationRecord.getNotes())
                .doctorName(vaccinationRecord.getDoctor() != null ? vaccinationRecord.getDoctor().getFullname() : null)
                .vaccineName(vaccinationRecord.getVaccine() !=  null ? vaccinationRecord.getVaccine().getName() : null)
                .vaccineManufacture(vaccinationRecord.getVaccine() !=  null ? vaccinationRecord.getVaccine().getManufacturer() : null)
                .vaccineDescription(vaccinationRecord.getVaccine() !=  null ? vaccinationRecord.getVaccine().getDescription() : null)
                .message("")
                .build();
    }

    private VaccinationRecordResponse convertToResponse(VaccinationRecord vaccinationRecord, String message){
        return VaccinationRecordResponse.builder()
                .vaccinationRecordId(vaccinationRecord.getId())
                .vaccinationDate(vaccinationRecord.getVaccinationDate())
                .nextDueDate(vaccinationRecord.getNextDueDate())
                .status(vaccinationRecord.getStatus())
                .notes(vaccinationRecord.getNotes())
                .doctorName(vaccinationRecord.getDoctor() != null ? vaccinationRecord.getDoctor().getFullname() : null)
                .vaccineName(vaccinationRecord.getVaccine() !=  null ? vaccinationRecord.getVaccine().getName() : null)
                .vaccineManufacture(vaccinationRecord.getVaccine() !=  null ? vaccinationRecord.getVaccine().getManufacturer() : null)
                .vaccineDescription(vaccinationRecord.getVaccine() !=  null ? vaccinationRecord.getVaccine().getDescription() : null)
                .message(message)
                .build();
    }
//    private Long vaccinationRecordId;
//    private LocalDate vaccinationDate;
//    private LocalDate nextDueDate;
//    private String status;
//    private String notes;
//    private String doctorName;
//    private String vaccineName;
//    private String vaccineManufacture;
//    private String vaccineDescription;
//    private String message;

}

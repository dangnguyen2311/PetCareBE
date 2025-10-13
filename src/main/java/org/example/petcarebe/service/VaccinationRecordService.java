package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.vaccinationrecord.CreateVaccinationRecordRequest;
import org.example.petcarebe.dto.request.vaccinationrecord.UpdateVaccinationRecordRequest;
import org.example.petcarebe.dto.response.vaccinationrecord.VaccinationRecordResponse;
import org.example.petcarebe.model.*;
import org.example.petcarebe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public VaccinationRecordResponse getVaccinationById(Long id) {
        VaccinationRecord vaccinationRecord = vaccinationRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination Record not found"));
        return convertToResponse(vaccinationRecord, "Vaccination Record get successfully");
    }

    public List<VaccinationRecordResponse> getALlVaccinationRecord() {
        List<VaccinationRecord> vaccinationRecordList = vaccinationRecordRepository.findAll();
        return vaccinationRecordList.stream()
                .map(this::convertToResponse)
                .toList();
    }

    public VaccinationRecordResponse updateVacciantionRecord(Long id, UpdateVaccinationRecordRequest request) {
        VaccinationRecord vaccinationRecordById = vaccinationRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination Record not found"));
        Visit visit = null;
        Vaccine vaccine = null;
        Pet pet = null;
        if (request.getVisitId() != null) {
            visit = visitRepository.findById(request.getVisitId())
                    .orElse(null);
        }
        if (request.getVaccineId() != null) {
            vaccine = vaccineRepository.findById(request.getVaccineId())
                    .orElse(null);
        }
        if (request.getPetId() != null) {
            pet = petRepository.findById(request.getPetId())
                    .orElse(null);
        }
        if(vaccine != null) vaccinationRecordById.setVaccine(vaccine);
        if(visit != null) vaccinationRecordById.setVisit(visit);
        if(pet != null) vaccinationRecordById.setPet(pet);
        vaccinationRecordById.setVaccinationDate(request.getVaccinationDate());
        vaccinationRecordById.setNextDueDate(request.getNextDueDate());
        vaccinationRecordById.setNotes(request.getNotes());
        vaccinationRecordById.setStatus(request.getStatus());

        VaccinationRecord savedVaccinationRecord = vaccinationRecordRepository.save(vaccinationRecordById);
        return convertToResponse(savedVaccinationRecord, "Vaccination Record updated successfully");
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
                .petName(vaccinationRecord.getPet() != null ? vaccinationRecord.getPet().getName() : null)
                .petType(vaccinationRecord.getPet() != null ? (vaccinationRecord.getPet().getAnimalType() != null ? vaccinationRecord.getPet().getAnimalType().getName() : null) : null)
                .visitId(vaccinationRecord.getVisit() != null ? vaccinationRecord.getVisit().getId() : null)
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
                .petName(vaccinationRecord.getPet() != null ? vaccinationRecord.getPet().getName() : null)
                .petType(vaccinationRecord.getPet() != null ? (vaccinationRecord.getPet().getAnimalType() != null ? vaccinationRecord.getPet().getAnimalType().getName() : null) : null)
                .visitId(vaccinationRecord.getVisit() != null ? vaccinationRecord.getVisit().getId() : null)
                .message(message)
                .build();
    }



}

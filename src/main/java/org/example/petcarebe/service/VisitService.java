package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.visit.CreateVisitRequest;
import org.example.petcarebe.dto.response.visit.CreateVisitResponse;
import org.example.petcarebe.enums.AppointmentStatus;
import org.example.petcarebe.model.*;
import org.example.petcarebe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class VisitService {
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private WorkScheduleRepository workScheduleRepository;
    @Autowired
    private ClinicRoomRepository  clinicRoomRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PetRepository  petRepository;

    public CreateVisitResponse createVisit(Long id, CreateVisitRequest request) {
        WorkSchedule workSchedule = workScheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Work Schedule Not Found"));
        ClinicRoom clinicRoom =  clinicRoomRepository.findById(id).orElseThrow(() -> new RuntimeException("ClinicRoom Not Found"));
        Appointment appointment = appointmentRepository.findById(id).orElse(null);

        if(appointment != null && appointment.getStatus() != AppointmentStatus.IN_PROGRESS) {
            throw new RuntimeException("Appointment status is not valid");

        }
        Pet pet = petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet Not Found")); // timf thoong qua Customer
        Visit visit = new Visit();
        visit.setVisitDate(appointment != null ? appointment.getAppointmentDate(): null);
        visit.setReasonVisit(request.getReasonVisit());
        visit.setVisitTime(appointment != null ? appointment.getAppointmentTime(): null);
        visit.setRoomNumber(request.getRoomNumber());
        visit.setNotes(request.getNotes());
        visit.setWorkSchedule(workSchedule);
        visit.setClinicRoom(clinicRoom);
        visit.setAppointment(appointment);
        visit.setPet(pet);

        Visit savedVisit = visitRepository.save(visit);
        return convertToCreateresponse(savedVisit);

    }

    public CreateVisitResponse convertToCreateresponse(Visit visit) {
        return CreateVisitResponse.builder()
                .id(visit.getId())
                .visitDate(visit.getVisitDate())
                .reasonVisit(visit.getReasonVisit())
                .visitTime(visit.getVisitTime())
                .queueNumber(visit.getAppointment().getQueueNumber())
                .roomNumber(visit.getRoomNumber())
                .notes(visit.getNotes())
                .workScheduleId(visit.getWorkSchedule().getId())
                .doctorName(visit.getWorkSchedule().getDoctor().getFullname())
                .clinicRoomId(visit.getClinicRoom().getId())
                .clinicRoomName(visit.getClinicRoom().getName())
                .petId(visit.getPet().getId())
                .petName(visit.getPet().getName())
                .message("Visit created successfully, time: " + visit.getVisitTime())
                .build();


    }
    public CreateVisitResponse convertToCreateresponse(Visit visit, String message) {
        return CreateVisitResponse.builder()
                .id(visit.getId())
                .visitDate(visit.getVisitDate())
                .reasonVisit(visit.getReasonVisit())
                .visitTime(visit.getVisitTime())
                .queueNumber(visit.getAppointment().getQueueNumber())
                .roomNumber(visit.getRoomNumber())
                .notes(visit.getNotes())
                .workScheduleId(visit.getWorkSchedule().getId())
                .doctorName(visit.getWorkSchedule().getDoctor().getFullname())
                .clinicRoomId(visit.getClinicRoom().getId())
                .clinicRoomName(visit.getClinicRoom().getName())
                .petId(visit.getPet().getId())
                .petName(visit.getPet().getName())
                .message(message)
                .build();


    }
}


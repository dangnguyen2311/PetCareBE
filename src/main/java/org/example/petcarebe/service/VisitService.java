package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.visit.CreateVisitRequest;
import org.example.petcarebe.dto.request.visit.UpdateVisitRequest;
import org.example.petcarebe.dto.request.visit.VisitDateRangeRequest;
import org.example.petcarebe.dto.response.visit.CreateVisitResponse;
import org.example.petcarebe.dto.response.visit.VisitResponse;
import org.example.petcarebe.dto.response.visit.VisitMedicalSummaryResponse;
import org.example.petcarebe.dto.response.visit.VisitStatisticsResponse;
import org.example.petcarebe.dto.response.diagnosis.DiagnosisResponse;
import org.example.petcarebe.dto.response.testresult.TestResultResponse;
import org.example.petcarebe.enums.AppointmentStatus;
import org.example.petcarebe.model.*;
import org.example.petcarebe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private DiagnosisRepository diagnosisRepository;
    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private DiagnosisService diagnosisService;
    @Autowired
    private TestResultService testResultService;

    public CreateVisitResponse createVisit(CreateVisitRequest request) {
        WorkSchedule workSchedule = workScheduleRepository.findById(request.getWorkScheduleId()).orElseThrow(() -> new RuntimeException("Work Schedule Not Found"));
        ClinicRoom clinicRoom =  clinicRoomRepository.findById(request.getClinicRoomId()).orElseThrow(() -> new RuntimeException("ClinicRoom Not Found"));
        // Truowngf hop ddi khams truwcj tieeps
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId()).orElse(null);

        if(appointment != null && appointment.getStatus() != AppointmentStatus.INPROGRESS) {
            throw new RuntimeException("Appointment status is not valid");

        }
        Pet pet = petRepository.findById(request.getPetId()).orElseThrow(() -> new RuntimeException("Pet Not Found")); // timf thoong qua Customer
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
        return convertToCreateresponse(savedVisit, "Visit Created Successfully");

    }

    public CreateVisitResponse convertToCreateresponse(Visit visit) {
        return CreateVisitResponse.builder()
                .id(visit.getId())
                .visitDate(visit.getVisitDate())
                .reasonVisit(visit.getReasonVisit())
                .visitTime(visit.getVisitTime())
                .queueNumber(visit.getAppointment() != null ? visit.getAppointment().getQueueNumber() : null)
                .roomNumber(visit.getRoomNumber())
                .notes(visit.getNotes())
                .workScheduleId(visit.getWorkSchedule() != null ? visit.getWorkSchedule().getId() : null)
                .doctorName(visit.getWorkSchedule() != null ? (visit.getWorkSchedule().getDoctor() != null ? visit.getWorkSchedule().getDoctor().getFullname() : null) : null )
                .clinicRoomId(visit.getClinicRoom() != null ? visit.getClinicRoom().getId() : null)
                .clinicRoomName(visit.getClinicRoom() != null ? visit.getClinicRoom().getName() : null)
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
                .queueNumber(visit.getAppointment() != null ? visit.getAppointment().getQueueNumber() : null)
                .roomNumber(visit.getRoomNumber())
                .notes(visit.getNotes())
                .workScheduleId(visit.getWorkSchedule() != null ? visit.getWorkSchedule().getId() : null)
                .doctorName(visit.getWorkSchedule() != null ? (visit.getWorkSchedule().getDoctor() != null ? visit.getWorkSchedule().getDoctor().getFullname() : null) : null )
                .clinicRoomId(visit.getClinicRoom() != null ? visit.getClinicRoom().getId() : null)
                .clinicRoomName(visit.getClinicRoom() != null ? visit.getClinicRoom().getName() : null)
                .petId(visit.getPet().getId())
                .petName(visit.getPet().getName())
                .message(message)
                .build();


    }

    /**
     * Get all visits
     */
    public List<VisitResponse> getAllVisits() {
        List<Visit> visits = visitRepository.findAll();
        return visits.stream()
                .map(this::convertToVisitResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get visit by ID
     */
    public VisitResponse getVisitById(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found with id: " + id));
        return convertToVisitResponse(visit);
    }

    /**
     * Update visit
     */
    @Transactional
    public VisitResponse updateVisit(Long id, UpdateVisitRequest request) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found with id: " + id));

        // Update fields if provided
        if (request.getVisitDate() != null) {
            visit.setVisitDate(request.getVisitDate());
        }
        if (request.getVisitTime() != null) {
            visit.setVisitTime(request.getVisitTime());
        }
        if (request.getReasonVisit() != null) {
            visit.setReasonVisit(request.getReasonVisit());
        }
        if (request.getRoomNumber() != null) {
            visit.setRoomNumber(request.getRoomNumber());
        }
        if (request.getNotes() != null) {
            visit.setNotes(request.getNotes());
        }
        if (request.getWorkScheduleId() != null) {
            WorkSchedule workSchedule = workScheduleRepository.findById(request.getWorkScheduleId())
                    .orElseThrow(() -> new RuntimeException("Work Schedule not found"));
            visit.setWorkSchedule(workSchedule);
        }
        if (request.getClinicRoomId() != null) {
            ClinicRoom clinicRoom = clinicRoomRepository.findById(request.getClinicRoomId())
                    .orElseThrow(() -> new RuntimeException("Clinic Room not found"));
            visit.setClinicRoom(clinicRoom);
        }

        Visit updatedVisit = visitRepository.save(visit);
        return convertToVisitResponse(updatedVisit, "Visit updated successfully");
    }

    /**
     * Delete visit
     */
    @Transactional
    public void deleteVisit(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found with id: " + id));
        visitRepository.delete(visit);
    }

    /**
     * Get visits by pet ID
     */
    public List<VisitResponse> getVisitsByPetId(Long petId) {
        List<Visit> visits = visitRepository.findByPetId(petId);
        return visits.stream()
                .map(this::convertToVisitResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get visits by customer ID
     */
    public List<VisitResponse> getVisitsByCustomerId(Long customerId) {
        List<Visit> visits = visitRepository.findByCustomerId(customerId);
        return visits.stream()
                .map(this::convertToVisitResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get visits by doctor ID
     */
    public List<VisitResponse> getVisitsByDoctorId(Long doctorId) {
        List<Visit> visits = visitRepository.findByDoctorId(doctorId);
        return visits.stream()
                .map(this::convertToVisitResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get visits by date
     */
    public List<VisitResponse> getVisitsByDate(LocalDate date) {
        List<Visit> visits = visitRepository.findByVisitDate(date);
        return visits.stream()
                .map(this::convertToVisitResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get visits by date range
     */
    public List<VisitResponse> getVisitsByDateRange(VisitDateRangeRequest request) {
        List<Visit> visits = visitRepository.findByVisitDateBetween(
                request.getStartDate(), request.getEndDate());
        return visits.stream()
                .map(this::convertToVisitResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get diagnoses for a visit
     */
    public List<DiagnosisResponse> getVisitDiagnoses(Long visitId) {
        // Verify visit exists
        visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found with id: " + visitId));

        return diagnosisService.getDiagnosesByVisitId(visitId);
    }

    /**
     * Get test results for a visit
     */
    public List<TestResultResponse> getVisitTestResults(Long visitId) {
        // Verify visit exists
        visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found with id: " + visitId));

        return testResultService.getTestResultsByVisitId(visitId);
    }

    /**
     * Get medical summary for a visit
     */
    public VisitMedicalSummaryResponse getVisitMedicalSummary(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found with id: " + visitId));

        List<DiagnosisResponse> diagnoses = diagnosisService.getDiagnosesByVisitId(visitId);
        List<TestResultResponse> testResults = testResultService.getTestResultsByVisitId(visitId);

        return VisitMedicalSummaryResponse.builder()
                .visitId(visit.getId())
                .visitDate(visit.getVisitDate())
                .visitTime(visit.getVisitTime())
                .reasonVisit(visit.getReasonVisit())
                .notes(visit.getNotes())
                .petId(visit.getPet().getId())
                .petName(visit.getPet().getName())
                .petBreed(visit.getPet().getBreed())
                .petType(visit.getPet().getAnimalType() != null ? visit.getPet().getAnimalType().getName() : null)
                .customerId(visit.getPet().getCustomer().getId())
                .customerName(visit.getPet().getCustomer().getFullname())
                .doctorId(visit.getWorkSchedule().getDoctor().getId())
                .doctorName(visit.getWorkSchedule().getDoctor().getFullname())
                .doctorSpecialization(visit.getWorkSchedule().getDoctor().getSpecialization())
                .diagnoses(diagnoses)
                .testResults(testResults)
                .totalDiagnoses(diagnoses.size())
                .totalTestResults(testResults.size())
                .message("Medical summary retrieved successfully")
                .build();
    }

    /**
     * Convert Visit entity to VisitResponse
     */
    private VisitResponse convertToVisitResponse(Visit visit) {
        return convertToVisitResponse(visit, null);
    }

    /**
     * Convert Visit entity to VisitResponse with custom message
     */
    private VisitResponse convertToVisitResponse(Visit visit, String message) {
        // Check if visit has medical records
        boolean hasDiagnoses = !diagnosisRepository.findByVisitId(visit.getId()).isEmpty();
        boolean hasTestResults = !testResultRepository.findByVisitId(visit.getId()).isEmpty();

        return VisitResponse.builder()
                .id(visit.getId())
                .visitDate(visit.getVisitDate())
                .visitTime(visit.getVisitTime())
                .reasonVisit(visit.getReasonVisit())
                .roomNumber(visit.getRoomNumber())
                .notes(visit.getNotes())
                .workScheduleId(visit.getWorkSchedule() != null ? visit.getWorkSchedule().getId() : null)
                .doctorName(visit.getWorkSchedule() != null && visit.getWorkSchedule().getDoctor() != null ?
                           visit.getWorkSchedule().getDoctor().getFullname() : null)
                .doctorSpecialization(visit.getWorkSchedule() != null && visit.getWorkSchedule().getDoctor() != null ?
                                    visit.getWorkSchedule().getDoctor().getSpecialization() : null)
                .doctorPhone(visit.getWorkSchedule() != null && visit.getWorkSchedule().getDoctor() != null ?
                           visit.getWorkSchedule().getDoctor().getPhone() : null)
                .clinicRoomId(visit.getClinicRoom() != null ? visit.getClinicRoom().getId() : null)
                .clinicRoomName(visit.getClinicRoom() != null ? visit.getClinicRoom().getName() : null)
                .petId(visit.getPet().getId())
                .petName(visit.getPet().getName())
                .petBreed(visit.getPet().getBreed())
                .petType(visit.getPet().getAnimalType() != null ? visit.getPet().getAnimalType().getName() : null)
                .customerId(visit.getPet().getCustomer().getId())
                .customerName(visit.getPet().getCustomer().getFullname())
                .customerEmail(visit.getPet().getCustomer().getEmail())
                .customerPhone(visit.getPet().getCustomer().getPhone())
                .appointmentId(visit.getAppointment() != null ? visit.getAppointment().getId() : null)
                .queueNumber(visit.getAppointment() != null ? visit.getAppointment().getQueueNumber() : null)
                .appointmentStatus(visit.getAppointment() != null ? visit.getAppointment().getStatus().toString() : null)
                .hasDiagnoses(hasDiagnoses)
                .hasTestResults(hasTestResults)
                .hasPrescriptions(false) // TODO: Implement prescription check if needed
                .message(message != null ? message : "Visit retrieved successfully")
                .build();
    }

    /**
     * Get daily visit statistics
     */
    public VisitStatisticsResponse getDailyStatistics(VisitDateRangeRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        List<Object[]> dailyStats = visitRepository.getDailyVisitStatistics(startDate, endDate);
        Map<LocalDate, Long> dailyVisitCounts = new HashMap<>();

        for (Object[] stat : dailyStats) {
            LocalDate date = (LocalDate) stat[0];
            Long count = (Long) stat[1];
            dailyVisitCounts.put(date, count);
        }

        Long totalVisits = visitRepository.countByVisitDateBetween(startDate, endDate);

        return VisitStatisticsResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalVisits(totalVisits)
                .dailyVisitCounts(dailyVisitCounts)
                .message("Daily statistics retrieved successfully")
                .build();
    }

    /**
     * Get monthly visit statistics
     */
    public VisitStatisticsResponse getMonthlyStatistics(VisitDateRangeRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        List<Object[]> monthlyStats = visitRepository.getMonthlyVisitStatistics(startDate, endDate);
        Map<String, Long> monthlyVisitCounts = new HashMap<>();

        for (Object[] stat : monthlyStats) {
            Integer year = (Integer) stat[0];
            Integer month = (Integer) stat[1];
            Long count = (Long) stat[2];
            String yearMonth = year + "-" + String.format("%02d", month);
            monthlyVisitCounts.put(yearMonth, count);
        }

        Long totalVisits = visitRepository.countByVisitDateBetween(startDate, endDate);

        return VisitStatisticsResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalVisits(totalVisits)
                .monthlyVisitCounts(monthlyVisitCounts)
                .message("Monthly statistics retrieved successfully")
                .build();
    }

    /**
     * Get doctor performance statistics
     */
    public VisitStatisticsResponse getDoctorPerformanceStatistics(VisitDateRangeRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        List<Object[]> doctorStats = visitRepository.getDoctorPerformanceStatistics(startDate, endDate);
        List<VisitStatisticsResponse.DoctorPerformanceData> doctorPerformance = new ArrayList<>();

        for (Object[] stat : doctorStats) {
            Long doctorId = (Long) stat[0];
            String doctorName = (String) stat[1];
            String specialization = (String) stat[2];
            Long visitCount = (Long) stat[3];

            doctorPerformance.add(VisitStatisticsResponse.DoctorPerformanceData.builder()
                    .doctorId(doctorId)
                    .doctorName(doctorName)
                    .specialization(specialization)
                    .visitCount(visitCount)
                    .build());
        }

        // Get additional statistics
        List<Object[]> petStats = visitRepository.getMostVisitedPets(startDate, endDate);
        List<VisitStatisticsResponse.PetVisitData> mostVisitedPets = new ArrayList<>();

        for (Object[] stat : petStats) {
            Long petId = (Long) stat[0];
            String petName = (String) stat[1];
            String petType = (String) stat[2];
            Long visitCount = (Long) stat[3];

            mostVisitedPets.add(VisitStatisticsResponse.PetVisitData.builder()
                    .petId(petId)
                    .petName(petName)
                    .petType(petType)
                    .visitCount(visitCount)
                    .build());
        }

        List<Object[]> roomStats = visitRepository.getClinicRoomUtilization(startDate, endDate);
        List<VisitStatisticsResponse.ClinicRoomUtilizationData> clinicRoomUtilization = new ArrayList<>();

        for (Object[] stat : roomStats) {
            Long roomId = (Long) stat[0];
            String roomName = (String) stat[1];
            Long visitCount = (Long) stat[2];

            clinicRoomUtilization.add(VisitStatisticsResponse.ClinicRoomUtilizationData.builder()
                    .clinicRoomId(roomId)
                    .clinicRoomName(roomName)
                    .visitCount(visitCount)
                    .build());
        }

        Long totalVisits = visitRepository.countByVisitDateBetween(startDate, endDate);

        return VisitStatisticsResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalVisits(totalVisits)
                .doctorPerformance(doctorPerformance)
                .mostVisitedPets(mostVisitedPets)
                .clinicRoomUtilization(clinicRoomUtilization)
                .message("Doctor performance statistics retrieved successfully")
                .build();
    }
}


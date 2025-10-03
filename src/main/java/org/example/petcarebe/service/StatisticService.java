package org.example.petcarebe.service;

import org.example.petcarebe.dto.response.statistic.DoctorDashboardResponse;
import org.example.petcarebe.dto.response.statistic.StaffDashboardResponse;
import org.example.petcarebe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class StatisticService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TestResultRepository  testResultRepository;
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public DoctorDashboardResponse getDoctorDashboard() {
        Integer unfinishedTestTodayNums = testResultRepository.getTestResultsByStatusAndCreatedDate("UNDONE", LocalDate.now(), Sort.by(Sort.Order.desc("created_date")));
        Integer appointmentTodayNums = appointmentRepository.getAppointmentsByAppointmentDate(LocalDate.now(), Sort.by(Sort.Order.desc("created_date")));
        Integer petsTodayNeedExamNums = visitRepository.getPetsNeedExamToday(LocalDate.now());
        Integer vaccineToBeAdministeredNums = vaccinationRecordRepository.getVaccinationRecordsByStatus("UNVACCINATED", Sort.by(Sort.Order.desc("created_date")));
        Integer visitNums = visitRepository.getVisitByVisitDate(LocalDate.now(), Sort.by(Sort.Order.desc("visit_date")));
        Integer prescriptionTodayNums = prescriptionRepository.getPrescriptionsByCreatedDate(LocalDate.now(),  Sort.by(Sort.Order.desc("created_date")));

        return DoctorDashboardResponse.builder()
                .unfinishedTestTodayNums(unfinishedTestTodayNums)
                .appointmentTodayNums(appointmentTodayNums)
                .petsTodayNeedExamNums(petsTodayNeedExamNums)
                .vaccineToBeAdministeredNums(vaccineToBeAdministeredNums)
                .visitNums(visitNums)
                .prescriptionTodayNums(prescriptionTodayNums)
                .message("DoctorDashboardResponse")
                .build();
    }

    public StaffDashboardResponse getStaffDashboard() {

        return StaffDashboardResponse.builder().build();
    }
//    private Integer unfinishedTestTodayNums;
//    private Integer appointmentTodayNums;
//    private Integer petsTodayNeedExamNums;
//    private Integer vaccineToBeAdministeredNums;
//    private Integer visitNums;
//    private Integer prescriptionTodayNums;
//    private String message;
}

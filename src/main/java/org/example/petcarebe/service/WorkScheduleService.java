package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.workschedule.CreateWorkScheduleRequest;
import org.example.petcarebe.dto.request.workschedule.UpdateWorkScheduleRequest;
import org.example.petcarebe.dto.response.workschedule.WorkScheduleResponse;
import org.example.petcarebe.dto.response.workschedule.WorkScheduleStatisticsResponse;
import org.example.petcarebe.model.Doctor;
import org.example.petcarebe.model.WorkSchedule;
import org.example.petcarebe.repository.DoctorRepository;
import org.example.petcarebe.repository.WorkScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkScheduleService {

    @Autowired
    private WorkScheduleRepository workScheduleRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * Create a new work schedule
     */
    @Transactional
    public WorkScheduleResponse createWorkSchedule(CreateWorkScheduleRequest request) {
        // Validate doctor exists
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + request.getDoctorId()));

        // Validate time range
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("Start time must be before end time");
        }

        // Check for existing schedule on same day for same doctor
        Optional<WorkSchedule> existingSchedule = workScheduleRepository
                .findByDoctorIdAndWorkDate(request.getDoctorId(), request.getWorkDate());
        if (existingSchedule.isPresent()) {
            throw new RuntimeException("Doctor already has a schedule for " + request.getWorkDate());
        }



        // Check for overlapping schedules
        List<WorkSchedule> overlappingSchedules = workScheduleRepository.findOverlappingSchedules(
                request.getDoctorId(), request.getWorkDate(),
                request.getStartTime(), request.getEndTime(), -1L);
        if (!overlappingSchedules.isEmpty()) {
            throw new RuntimeException("Schedule overlaps with existing schedule");
        }

        WorkSchedule workSchedule = new WorkSchedule();
        workSchedule.setDoctor(doctor);
        workSchedule.setWorkDate(request.getWorkDate());
        workSchedule.setStartTime(request.getStartTime());
        workSchedule.setEndTime(request.getEndTime());
        workSchedule.setNotes(request.getNotes());

        WorkSchedule savedSchedule = workScheduleRepository.save(workSchedule);
        return convertToResponse(savedSchedule, "Work schedule created successfully");
    }

    /**
     * Get all work schedules
     */
    public List<WorkScheduleResponse> getAllWorkSchedules() {
        return workScheduleRepository.findAllOrderByDoctorAndDate().stream()
                .map(schedule -> convertToResponse(schedule, null))
                .collect(Collectors.toList());
    }

    /**
     * Get work schedule by ID
     */
    public WorkScheduleResponse getWorkScheduleById(Long id) {
        WorkSchedule workSchedule = workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work schedule not found with id: " + id));
        return convertToResponse(workSchedule, null);
    }

    /**
     * Get work schedules by doctor ID
     */
    public List<WorkScheduleResponse> getWorkSchedulesByDoctorId(Long doctorId) {
        // Validate doctor exists
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        return workScheduleRepository.findByDoctorIdOrderByDayOfWeek(doctorId).stream()
                .map(schedule -> convertToResponse(schedule, null))
                .collect(Collectors.toList());
    }



    /**
     * Get work schedules by specific work date
     */
    public List<WorkScheduleResponse> getWorkSchedulesByDate(LocalDate workDate) {
        return workScheduleRepository.findByWorkDate(workDate).stream()
                .map(schedule -> convertToResponse(schedule, null))
                .collect(Collectors.toList());
    }

    /**
     * Get work schedules by date range
     */
    public List<WorkScheduleResponse> getWorkSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        return workScheduleRepository.findByWorkDateBetween(startDate, endDate).stream()
                .map(schedule -> convertToResponse(schedule, null))
                .collect(Collectors.toList());
    }

    /**
     * Get work schedules by doctor and date range
     */
    public List<WorkScheduleResponse> getWorkSchedulesByDoctorAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate) {
        // Validate doctor exists
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        return workScheduleRepository.findByDoctorIdAndWorkDateBetween(doctorId, startDate, endDate).stream()
                .map(schedule -> convertToResponse(schedule, null))
                .collect(Collectors.toList());
    }

    /**
     * Update work schedule
     */
    @Transactional
    public WorkScheduleResponse updateWorkSchedule(Long id, UpdateWorkScheduleRequest request) {
        WorkSchedule workSchedule = workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work schedule not found with id: " + id));

        // Validate time range if both times are provided
        if (request.getStartTime() != null && request.getEndTime() != null) {
            if (request.getStartTime().isAfter(request.getEndTime())) {
                throw new RuntimeException("Start time must be before end time");
            }
        }

        // Check for overlapping schedules if time or date is being changed
        if (request.getWorkDate() != null || request.getStartTime() != null || request.getEndTime() != null) {
            LocalDate workDate = request.getWorkDate() != null ? request.getWorkDate() : workSchedule.getWorkDate();
            LocalTime startTime = request.getStartTime() != null ? request.getStartTime() : workSchedule.getStartTime();
            LocalTime endTime = request.getEndTime() != null ? request.getEndTime() : workSchedule.getEndTime();

            List<WorkSchedule> overlappingSchedules = workScheduleRepository.findOverlappingSchedules(
                    workSchedule.getDoctor().getId(), workDate, startTime, endTime, id);
            if (!overlappingSchedules.isEmpty()) {
                throw new RuntimeException("Updated schedule would overlap with existing schedule");
            }
        }

        // Update fields if provided
        if (request.getWorkDate() != null) {
            workSchedule.setWorkDate(request.getWorkDate());
        }
        if (request.getStartTime() != null) {
            workSchedule.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            workSchedule.setEndTime(request.getEndTime());
        }
        if (request.getNotes() != null) {
            workSchedule.setNotes(request.getNotes());
        }

        WorkSchedule updatedSchedule = workScheduleRepository.save(workSchedule);
        return convertToResponse(updatedSchedule, "Work schedule updated successfully");
    }

    /**
     * Delete work schedule
     */
    @Transactional
    public WorkScheduleResponse deleteWorkSchedule(Long id) {
        WorkSchedule workSchedule = workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work schedule not found with id: " + id));

        // TODO: Check if there are any visits scheduled for this work schedule

        workScheduleRepository.delete(workSchedule);
        return convertToResponse(workSchedule, "Work schedule deleted successfully");
    }

    /**
     * Get work schedule statistics for a doctor
     */
    public WorkScheduleStatisticsResponse getWorkScheduleStatistics(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        List<WorkSchedule> schedules = workScheduleRepository.findByDoctorIdOrderByDayOfWeek(doctorId);

        if (schedules.isEmpty()) {
            return WorkScheduleStatisticsResponse.builder()
                    .doctorId(doctorId)
                    .doctorName(doctor.getFullname())
                    .specialization(doctor.getSpecialization())
                    .totalWorkingDays(0)
                    .totalWorkingHours(0)
                    .averageHoursPerDay(0.0)
                    .scheduleList(List.of())
                    .totalVisitsScheduled(0)
                    .totalVisitsCompleted(0)
                    .scheduleUtilizationRate(0.0)
                    .earliestStartTime("N/A")
                    .latestEndTime("N/A")
                    .mostBusyDate("N/A")
                    .leastBusyDate("N/A")
                    .build();
        }

        // Calculate statistics
        int totalWorkingDays = schedules.size();
        int totalWorkingHours = schedules.stream()
                .mapToInt(schedule -> (int) Duration.between(schedule.getStartTime(), schedule.getEndTime()).toHours())
                .sum();
        double averageHoursPerDay = totalWorkingDays > 0 ? (double) totalWorkingHours / totalWorkingDays : 0.0;

        // Find earliest start time and latest end time
        LocalTime earliestStart = schedules.stream()
                .map(WorkSchedule::getStartTime)
                .min(LocalTime::compareTo)
                .orElse(LocalTime.MIDNIGHT);

        LocalTime latestEnd = schedules.stream()
                .map(WorkSchedule::getEndTime)
                .max(LocalTime::compareTo)
                .orElse(LocalTime.MIDNIGHT);

        // Create schedule breakdown
        List<WorkScheduleStatisticsResponse.DayScheduleInfo> scheduleList = schedules.stream()
                .map(schedule -> {
                    int hours = (int) Duration.between(schedule.getStartTime(), schedule.getEndTime()).toHours();
                    return new WorkScheduleStatisticsResponse.DayScheduleInfo(
                            schedule.getWorkDate().toString(),
                            schedule.getStartTime().toString(),
                            schedule.getEndTime().toString(),
                            hours,
                            0, // TODO: Calculate actual scheduled visits
                            schedule.getNotes()
                    );
                })
                .collect(Collectors.toList());

        // Find busiest and least busy dates (by working hours)
        String mostBusyDate = schedules.stream()
                .max((s1, s2) -> {
                    long hours1 = Duration.between(s1.getStartTime(), s1.getEndTime()).toHours();
                    long hours2 = Duration.between(s2.getStartTime(), s2.getEndTime()).toHours();
                    return Long.compare(hours1, hours2);
                })
                .map(schedule -> schedule.getWorkDate().toString())
                .orElse("N/A");

        String leastBusyDate = schedules.stream()
                .min((s1, s2) -> {
                    long hours1 = Duration.between(s1.getStartTime(), s1.getEndTime()).toHours();
                    long hours2 = Duration.between(s2.getStartTime(), s2.getEndTime()).toHours();
                    return Long.compare(hours1, hours2);
                })
                .map(schedule -> schedule.getWorkDate().toString())
                .orElse("N/A");

        return WorkScheduleStatisticsResponse.builder()
                .doctorId(doctorId)
                .doctorName(doctor.getFullname())
                .specialization(doctor.getSpecialization())
                .totalWorkingDays(totalWorkingDays)
                .totalWorkingHours(totalWorkingHours)
                .averageHoursPerDay(averageHoursPerDay)
                .scheduleList(scheduleList)
                .totalVisitsScheduled(0) // TODO: Calculate from Visit entity
                .totalVisitsCompleted(0) // TODO: Calculate from Visit entity
                .scheduleUtilizationRate(0.0) // TODO: Calculate based on actual visits
                .earliestStartTime(earliestStart.toString())
                .latestEndTime(latestEnd.toString())
                .mostBusyDate(mostBusyDate)
                .leastBusyDate(leastBusyDate)
                .build();
    }

    /**
     * Check if doctor is owner of work schedule
     */
    public boolean isScheduleOwner(Long scheduleId, String username) {
        Optional<WorkSchedule> schedule = workScheduleRepository.findById(scheduleId);
        if (schedule.isPresent() && schedule.get().getDoctor() != null && schedule.get().getDoctor().getUser() != null) {
            return schedule.get().getDoctor().getUser().getUsername().equals(username);
        }
        return false;
    }

    /**
     * Convert WorkSchedule entity to WorkScheduleResponse
     */
    private WorkScheduleResponse convertToResponse(WorkSchedule workSchedule, String message) {
        WorkScheduleResponse response = new WorkScheduleResponse();
        response.setId(workSchedule.getId());
        response.setWorkDate(workSchedule.getWorkDate());
        response.setStartTime(workSchedule.getStartTime());
        response.setEndTime(workSchedule.getEndTime());
        response.setNotes(workSchedule.getNotes());

        // Calculate total working hours
        Duration duration = Duration.between(workSchedule.getStartTime(), workSchedule.getEndTime());
        response.setTotalHours((int) duration.toHours());

        if (workSchedule.getDoctor() != null) {
            response.setDoctorId(workSchedule.getDoctor().getId());
            response.setDoctorName(workSchedule.getDoctor().getFullname());
            response.setDoctorSpecialization(workSchedule.getDoctor().getSpecialization());
            response.setDoctorPhone(workSchedule.getDoctor().getPhone());
            response.setDoctorEmail(workSchedule.getDoctor().getEmail());
        }

        response.setMessage(message);
        return response;
    }
}


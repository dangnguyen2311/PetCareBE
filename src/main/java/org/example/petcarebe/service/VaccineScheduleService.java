package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.vaccineschedule.CreateVaccineScheduleRequest;
import org.example.petcarebe.dto.request.vaccineschedule.UpdateVaccineScheduleRequest;
import org.example.petcarebe.dto.request.vaccineschedule.RescheduleVaccineRequest;
import org.example.petcarebe.dto.request.vaccineschedule.VaccineScheduleDateRangeRequest;
import org.example.petcarebe.dto.response.vaccineschedule.VaccineScheduleResponse;
import org.example.petcarebe.dto.response.vaccineschedule.VaccineScheduleStatisticsResponse;
import org.example.petcarebe.model.VaccineSchedule;
import org.example.petcarebe.model.Vaccine;
import org.example.petcarebe.model.Pet;
import org.example.petcarebe.repository.VaccineScheduleRepository;
import org.example.petcarebe.repository.VaccineRepository;
import org.example.petcarebe.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VaccineScheduleService {

    @Autowired
    private VaccineScheduleRepository vaccineScheduleRepository;

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private PetRepository petRepository;

    /**
     * Create a new vaccine schedule
     */
    @Transactional
    public VaccineScheduleResponse createVaccineSchedule(CreateVaccineScheduleRequest request) {
        // Validate pet exists
        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + request.getPetId()));

        // Validate vaccine exists
        Vaccine vaccine = vaccineRepository.findById(request.getVaccineId())
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id: " + request.getVaccineId()));

        // Check if pet already has a schedule for this vaccine
        Boolean existingSchedule = vaccineScheduleRepository.existsByPetIdAndVaccineId(
                request.getPetId(), request.getVaccineId());
        if (existingSchedule) {
            throw new RuntimeException("Pet already has a schedule for this vaccine");
        }

        // Validate scheduled date is not in the past
        if (request.getScheduledDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Scheduled date cannot be in the past");
        }

        VaccineSchedule vaccineSchedule = new VaccineSchedule();
        vaccineSchedule.setPet(pet);
        vaccineSchedule.setVaccine(vaccine);
        vaccineSchedule.setScheduledDate(request.getScheduledDate());
        vaccineSchedule.setStatus(request.getStatus() != null ? request.getStatus() : "SCHEDULED");

        VaccineSchedule savedSchedule = vaccineScheduleRepository.save(vaccineSchedule);
        return convertToResponse(savedSchedule, "Vaccine schedule created successfully");
    }

    /**
     * Get all vaccine schedules
     */
    public List<VaccineScheduleResponse> getAllVaccineSchedules() {
        List<VaccineSchedule> schedules = vaccineScheduleRepository.findAllOrderByDateAndStatus();
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get vaccine schedule by ID
     */
    public VaccineScheduleResponse getVaccineScheduleById(Long id) {
        VaccineSchedule schedule = vaccineScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine schedule not found with id: " + id));
        return convertToResponse(schedule);
    }

    /**
     * Update vaccine schedule
     */
    @Transactional
    public VaccineScheduleResponse updateVaccineSchedule(Long id, UpdateVaccineScheduleRequest request) {
        VaccineSchedule schedule = vaccineScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine schedule not found with id: " + id));

        // Update fields if provided
        if (request.getScheduledDate() != null) {
            // Validate new date is not in the past (unless status is COMPLETED)
            if (request.getScheduledDate().isBefore(LocalDate.now()) &&
                !"COMPLETED".equals(request.getStatus()) &&
                !"COMPLETED".equals(schedule.getStatus())) {
                throw new RuntimeException("Scheduled date cannot be in the past");
            }
            schedule.setScheduledDate(request.getScheduledDate());
        }
        if (request.getStatus() != null) {
            schedule.setStatus(request.getStatus());
        }

        VaccineSchedule updatedSchedule = vaccineScheduleRepository.save(schedule);
        return convertToResponse(updatedSchedule, "Vaccine schedule updated successfully");
    }

    /**
     * Delete vaccine schedule
     */
    @Transactional
    public void deleteVaccineSchedule(Long id) {
        VaccineSchedule schedule = vaccineScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine schedule not found with id: " + id));

        // Only allow deletion if status is SCHEDULED
        if (!"SCHEDULED".equals(schedule.getStatus())) {
            throw new RuntimeException("Cannot delete vaccine schedule with status: " + schedule.getStatus());
        }

        vaccineScheduleRepository.delete(schedule);
    }

    /**
     * Get vaccine schedules by pet ID
     */
    public List<VaccineScheduleResponse> getVaccineSchedulesByPetId(Long petId) {
        // Validate pet exists
        petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));

        List<VaccineSchedule> schedules = vaccineScheduleRepository.findByPetIdOrderByDate(petId);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get vaccine schedules by vaccine ID
     */
    public List<VaccineScheduleResponse> getVaccineSchedulesByVaccineId(Long vaccineId) {
        // Validate vaccine exists
        vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id: " + vaccineId));

        List<VaccineSchedule> schedules = vaccineScheduleRepository.findByVaccineId(vaccineId);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get vaccine schedules by status
     */
    public List<VaccineScheduleResponse> getVaccineSchedulesByStatus(String status) {
        List<VaccineSchedule> schedules = vaccineScheduleRepository.findByStatus(status);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get vaccine schedules by date
     */
    public List<VaccineScheduleResponse> getVaccineSchedulesByDate(LocalDate date) {
        List<VaccineSchedule> schedules = vaccineScheduleRepository.findByScheduledDate(date);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get vaccine schedules by date range
     */
    public List<VaccineScheduleResponse> getVaccineSchedulesByDateRange(VaccineScheduleDateRangeRequest request) {
        List<VaccineSchedule> schedules = vaccineScheduleRepository.findByScheduledDateBetween(
                request.getStartDate(), request.getEndDate());
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Complete vaccine schedule
     */
    @Transactional
    public VaccineScheduleResponse completeVaccineSchedule(Long id) {
        VaccineSchedule schedule = vaccineScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine schedule not found with id: " + id));

        if (!"SCHEDULED".equals(schedule.getStatus())) {
            throw new RuntimeException("Can only complete scheduled vaccine appointments");
        }

        schedule.setStatus("COMPLETED");
        VaccineSchedule updatedSchedule = vaccineScheduleRepository.save(schedule);
        return convertToResponse(updatedSchedule, "Vaccine schedule completed successfully");
    }

    /**
     * Reschedule vaccine
     */
    @Transactional
    public VaccineScheduleResponse rescheduleVaccine(Long id, RescheduleVaccineRequest request) {
        VaccineSchedule schedule = vaccineScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine schedule not found with id: " + id));

        if (!"SCHEDULED".equals(schedule.getStatus())) {
            throw new RuntimeException("Can only reschedule scheduled vaccine appointments");
        }

        // Validate new date is not in the past
        if (request.getNewScheduledDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("New scheduled date cannot be in the past");
        }

        schedule.setScheduledDate(request.getNewScheduledDate());

        VaccineSchedule updatedSchedule = vaccineScheduleRepository.save(schedule);
        return convertToResponse(updatedSchedule, "Vaccine schedule rescheduled successfully");
    }

    /**
     * Get overdue vaccine schedules
     */
    public List<VaccineScheduleResponse> getOverdueSchedules() {
        List<VaccineSchedule> schedules = vaccineScheduleRepository.findOverdueSchedules(LocalDate.now());
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming vaccine schedules (next 7 days)
     */
    public List<VaccineScheduleResponse> getUpcomingSchedules() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        List<VaccineSchedule> schedules = vaccineScheduleRepository.findUpcomingSchedules(startDate, endDate);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert VaccineSchedule entity to VaccineScheduleResponse
     */
    private VaccineScheduleResponse convertToResponse(VaccineSchedule schedule) {
        return convertToResponse(schedule, null);
    }

    /**
     * Convert VaccineSchedule entity to VaccineScheduleResponse with custom message
     */
    private VaccineScheduleResponse convertToResponse(VaccineSchedule schedule, String message) {
        LocalDate today = LocalDate.now();
        LocalDate scheduledDate = schedule.getScheduledDate();

        // Calculate days until/since scheduled
        long daysUntilScheduled = ChronoUnit.DAYS.between(today, scheduledDate);
        long daysSinceScheduled = ChronoUnit.DAYS.between(scheduledDate, today);

        // Determine if overdue or upcoming
        boolean isOverdue = scheduledDate.isBefore(today) && "SCHEDULED".equals(schedule.getStatus());
        boolean isUpcoming = scheduledDate.isAfter(today) && scheduledDate.isBefore(today.plusDays(7)) && "SCHEDULED".equals(schedule.getStatus());

        return VaccineScheduleResponse.builder()
                .id(schedule.getId())
                .scheduledDate(schedule.getScheduledDate())
                .status(schedule.getStatus())
                .vaccineId(schedule.getVaccine().getId())
                .vaccineName(schedule.getVaccine().getName())
                .vaccineManufacturer(schedule.getVaccine() != null ? schedule.getVaccine().getManufacturer() : null)
                .vaccineDescription(schedule.getVaccine() != null ? schedule.getVaccine().getDescription() : null)
                .petId(schedule.getPet().getId())
                .petName(schedule.getPet().getName())
                .petBreed(schedule.getPet().getBreed())
                .petType(schedule.getPet().getAnimalType() != null ? schedule.getPet().getAnimalType().getName() : null)
                .message(message != null ? message : "Vaccine schedule retrieved successfully")
                .build();
    }

    /**
     * Get vaccine schedule statistics
     */
    public VaccineScheduleStatisticsResponse getVaccineScheduleStatistics(VaccineScheduleDateRangeRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        // Get overall counts
        Long totalSchedules = vaccineScheduleRepository.countByScheduledDateBetween(startDate, endDate);
        Long overdueCount = vaccineScheduleRepository.countOverdueSchedules(LocalDate.now());
        LocalDate upcomingStart = LocalDate.now();
        LocalDate upcomingEnd = upcomingStart.plusDays(7);
        Long upcomingCount = vaccineScheduleRepository.countUpcomingSchedules(upcomingStart, upcomingEnd);

        // Get status distribution
        List<Object[]> statusStats = vaccineScheduleRepository.getScheduleStatisticsByStatus();
        Map<String, Long> statusDistribution = new HashMap<>();
        Long scheduledCount = 0L, completedCount = 0L, cancelledCount = 0L;

        for (Object[] stat : statusStats) {
            String status = (String) stat[0];
            Long count = (Long) stat[1];
            statusDistribution.put(status, count);

            switch (status) {
                case "SCHEDULED":
                    scheduledCount = count;
                    break;
                case "COMPLETED":
                    completedCount = count;
                    break;
                case "CANCELLED":
                    cancelledCount = count;
                    break;
            }
        }

        // Get daily statistics
        List<Object[]> dailyStats = vaccineScheduleRepository.getDailyScheduleStatistics(startDate, endDate);
        Map<LocalDate, Long> dailyScheduleCounts = new HashMap<>();
        for (Object[] stat : dailyStats) {
            LocalDate date = (LocalDate) stat[0];
            Long count = (Long) stat[1];
            dailyScheduleCounts.put(date, count);
        }

        // Get monthly statistics
        List<Object[]> monthlyStats = vaccineScheduleRepository.getMonthlyScheduleStatistics(startDate, endDate);
        Map<String, Long> monthlyScheduleCounts = new HashMap<>();
        for (Object[] stat : monthlyStats) {
            Integer year = (Integer) stat[0];
            Integer month = (Integer) stat[1];
            Long count = (Long) stat[2];
            String yearMonth = year + "-" + String.format("%02d", month);
            monthlyScheduleCounts.put(yearMonth, count);
        }

        // Get most scheduled vaccines
        List<Object[]> vaccineStats = vaccineScheduleRepository.getMostScheduledVaccines();
        List<VaccineScheduleStatisticsResponse.VaccineStatData> mostScheduledVaccines = new ArrayList<>();
        for (Object[] stat : vaccineStats) {
            Long vaccineId = (Long) stat[0];
            String vaccineName = (String) stat[1];
            Long scheduleCount = (Long) stat[2];

            mostScheduledVaccines.add(VaccineScheduleStatisticsResponse.VaccineStatData.builder()
                    .vaccineId(vaccineId)
                    .vaccineName(vaccineName)
                    .scheduleCount(scheduleCount)
                    .build());
        }

        // Get pets with most schedules
        List<Object[]> petStats = vaccineScheduleRepository.getPetsWithMostSchedules();
        List<VaccineScheduleStatisticsResponse.PetScheduleData> petsWithMostSchedules = new ArrayList<>();
        for (Object[] stat : petStats) {
            Long petId = (Long) stat[0];
            String petName = (String) stat[1];
            String petType = (String) stat[2];
            Long scheduleCount = (Long) stat[3];

            petsWithMostSchedules.add(VaccineScheduleStatisticsResponse.PetScheduleData.builder()
                    .petId(petId)
                    .petName(petName)
                    .petType(petType)
                    .scheduleCount(scheduleCount)
                    .build());
        }

        return VaccineScheduleStatisticsResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalSchedules(totalSchedules)
                .scheduledCount(scheduledCount)
                .completedCount(completedCount)
                .cancelledCount(cancelledCount)
                .overdueCount(overdueCount)
                .upcomingCount(upcomingCount)
                .statusDistribution(statusDistribution)
                .dailyScheduleCounts(dailyScheduleCounts)
                .monthlyScheduleCounts(monthlyScheduleCounts)
                .mostScheduledVaccines(mostScheduledVaccines)
                .petsWithMostSchedules(petsWithMostSchedules)
                .message("Vaccine schedule statistics retrieved successfully")
                .build();
    }
}

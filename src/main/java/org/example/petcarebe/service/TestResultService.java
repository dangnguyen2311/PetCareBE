package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.testresult.CreateTestResultRequest;
import org.example.petcarebe.dto.request.testresult.UpdateTestResultRequest;
import org.example.petcarebe.dto.response.testresult.TestResultResponse;
import org.example.petcarebe.dto.response.testresult.TestResultStatisticsResponse;
import org.example.petcarebe.model.TestResult;
import org.example.petcarebe.model.Visit;
import org.example.petcarebe.repository.TestResultRepository;
import org.example.petcarebe.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestResultService {

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private VisitRepository visitRepository;

    /**
     * Create a new test result
     */
    @Transactional
    public TestResultResponse createTestResult(CreateTestResultRequest request) {
        // Validate visit exists
        Visit visit = visitRepository.findById(request.getVisitId())
                .orElseThrow(() -> new RuntimeException("Visit not found with id: " + request.getVisitId()));

        TestResult testResult = new TestResult();
        testResult.setVisit(visit);
        testResult.setTestType(request.getTestType());
        testResult.setResult(request.getResult());
        testResult.setNotes(request.getNotes());
        testResult.setCreatedDate(LocalDate.now());

        TestResult savedTestResult = testResultRepository.save(testResult);
        return convertToResponse(savedTestResult, "Test result created successfully");
    }

    /**
     * Get all test results
     */
    public List<TestResultResponse> getAllTestResults() {
        return testResultRepository.findAllByOrderByCreatedDateDesc().stream()
                .map(testResult -> convertToResponse(testResult, null))
                .collect(Collectors.toList());
    }

    /**
     * Get test result by ID
     */
    public TestResultResponse getTestResultById(Long id) {
        TestResult testResult = testResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test result not found with id: " + id));
        return convertToResponse(testResult, null);
    }

    /**
     * Update test result
     */
    @Transactional
    public TestResultResponse updateTestResult(Long id, UpdateTestResultRequest request) {
        TestResult testResult = testResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test result not found with id: " + id));

        // Update test type if provided
        if (request.getTestType() != null && !request.getTestType().trim().isEmpty()) {
            testResult.setTestType(request.getTestType());
        }

        // Update result if provided
        if (request.getResult() != null && !request.getResult().trim().isEmpty()) {
            testResult.setResult(request.getResult());
        }

        // Update notes if provided
        if (request.getNotes() != null) {
            testResult.setNotes(request.getNotes());
        }

        TestResult updatedTestResult = testResultRepository.save(testResult);
        return convertToResponse(updatedTestResult, "Test result updated successfully");
    }

    /**
     * Delete test result
     */
    @Transactional
    public TestResultResponse deleteTestResult(Long id) {
        TestResult testResult = testResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test result not found with id: " + id));

        testResultRepository.delete(testResult);
        return convertToResponse(testResult, "Test result deleted successfully");
    }

    /**
     * Get test results by visit ID
     */
    public List<TestResultResponse> getTestResultsByVisitId(Long visitId) {
        return testResultRepository.findByVisitId(visitId).stream()
                .map(testResult -> convertToResponse(testResult, null))
                .collect(Collectors.toList());
    }

    /**
     * Get test results by test type
     */
    public List<TestResultResponse> getTestResultsByTestType(String testType) {
        return testResultRepository.findByTestTypeContainingIgnoreCase(testType).stream()
                .map(testResult -> convertToResponse(testResult, null))
                .collect(Collectors.toList());
    }

    /**
     * Get test results by pet ID
     */
    public List<TestResultResponse> getTestResultsByPetId(Long petId) {
        return testResultRepository.findByPetId(petId).stream()
                .map(testResult -> convertToResponse(testResult, null))
                .collect(Collectors.toList());
    }

    /**
     * Get test results by customer ID
     */
    public List<TestResultResponse> getTestResultsByCustomerId(Long customerId) {
        return testResultRepository.findByCustomerId(customerId).stream()
                .map(testResult -> convertToResponse(testResult, null))
                .collect(Collectors.toList());
    }

    /**
     * Get test results by doctor ID
     */
    public List<TestResultResponse> getTestResultsByDoctorId(Long doctorId) {
        return testResultRepository.findByDoctorId(doctorId).stream()
                .map(testResult -> convertToResponse(testResult, null))
                .collect(Collectors.toList());
    }

    /**
     * Get test results by date range
     */
    public List<TestResultResponse> getTestResultsByDateRange(LocalDate startDate, LocalDate endDate) {
        return testResultRepository.findByCreatedDateBetween(startDate, endDate).stream()
                .map(testResult -> convertToResponse(testResult, null))
                .collect(Collectors.toList());
    }

    /**
     * Search test results by result content
     */
    public List<TestResultResponse> searchTestResultsByResult(String keyword) {
        return testResultRepository.findByResultContainingIgnoreCase(keyword).stream()
                .map(testResult -> convertToResponse(testResult, null))
                .collect(Collectors.toList());
    }

    /**
     * Search test results by notes
     */
    public List<TestResultResponse> searchTestResultsByNotes(String keyword) {
        return testResultRepository.findByNotesContainingIgnoreCase(keyword).stream()
                .map(testResult -> convertToResponse(testResult, null))
                .collect(Collectors.toList());
    }

    /**
     * Get test result statistics
     */
    public TestResultStatisticsResponse getTestResultStatistics() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate yearStart = today.withDayOfYear(1);

        // Basic counts
        Long totalTestResults = testResultRepository.count();
        Long testResultsToday = testResultRepository.countByCreatedDate(today);
        Long testResultsThisWeek = testResultRepository.countByCreatedDateBetween(weekStart, today);
        Long testResultsThisMonth = testResultRepository.countByCreatedDateBetween(monthStart, today);
        Long testResultsThisYear = testResultRepository.countByCreatedDateBetween(yearStart, today);

        // Test type statistics
        List<Object[]> testTypeStats = testResultRepository.findMostCommonTestTypes();
        List<TestResultStatisticsResponse.TestTypeStatistic> topTestTypes = testTypeStats.stream()
                .limit(10)
                .map(row -> TestResultStatisticsResponse.TestTypeStatistic.builder()
                        .testType((String) row[0])
                        .count((Long) row[1])
                        .percentage(totalTestResults > 0 ? ((Long) row[1] * 100.0) / totalTestResults : 0.0)
                        .normalCount(0L) // TODO: Calculate based on result patterns
                        .abnormalCount(0L) // TODO: Calculate based on result patterns
                        .normalRate(0.0) // TODO: Calculate based on result patterns
                        .build())
                .collect(Collectors.toList());

        // Normal vs Abnormal results
        Object[] normalVsAbnormal = testResultRepository.countNormalVsAbnormalResults();
        Long normalResults = normalVsAbnormal[0] != null ? ((Number) normalVsAbnormal[0]).longValue() : 0L;
        Long abnormalResults = normalVsAbnormal[1] != null ? ((Number) normalVsAbnormal[1]).longValue() : 0L;
        Long totalCategorized = normalResults + abnormalResults;

        // Result patterns
        List<Object[]> patternStats = testResultRepository.findResultPatterns();
        List<TestResultStatisticsResponse.ResultPatternStatistic> resultPatterns = patternStats.stream()
                .map(row -> TestResultStatisticsResponse.ResultPatternStatistic.builder()
                        .pattern((String) row[0])
                        .count((Long) row[1])
                        .percentage(totalTestResults > 0 ? ((Long) row[1] * 100.0) / totalTestResults : 0.0)
                        .description(getPatternDescription((String) row[0]))
                        .build())
                .collect(Collectors.toList());

        // Doctor statistics
        List<Object[]> doctorStats = testResultRepository.findMostActiveDoctors();
        List<TestResultStatisticsResponse.DoctorStatistic> topDoctors = doctorStats.stream()
                .limit(5)
                .map(row -> TestResultStatisticsResponse.DoctorStatistic.builder()
                        .doctorId((Long) row[0])
                        .doctorName((String) row[1])
                        .specialization((String) row[2])
                        .testsCount((Long) row[3])
                        .percentage(totalTestResults > 0 ? ((Long) row[3] * 100.0) / totalTestResults : 0.0)
                        .mostCommonTestType("N/A") // TODO: Calculate most common test type per doctor
                        .build())
                .collect(Collectors.toList());

        return TestResultStatisticsResponse.builder()
                .totalTestResults(totalTestResults)
                .testResultsToday(testResultsToday)
                .testResultsThisWeek(testResultsThisWeek)
                .testResultsThisMonth(testResultsThisMonth)
                .testResultsThisYear(testResultsThisYear)
                .totalUniqueTestTypes((long) testTypeStats.size())
                .mostCommonTestType(topTestTypes.isEmpty() ? "N/A" : topTestTypes.getFirst().getTestType())
                .mostCommonTestTypeCount(topTestTypes.isEmpty() ? 0L : topTestTypes.get(0).getCount())
                .topTestTypes(topTestTypes)
                .normalResults(normalResults)
                .abnormalResults(abnormalResults)
                .normalResultPercentage(totalCategorized > 0 ? (normalResults * 100.0) / totalCategorized : 0.0)
                .abnormalResultPercentage(totalCategorized > 0 ? (abnormalResults * 100.0) / totalCategorized : 0.0)
                .resultPatterns(resultPatterns)
                .totalDoctorsInvolved((long) doctorStats.size())
                .mostActiveDoctorName(topDoctors.isEmpty() ? "N/A" : topDoctors.get(0).getDoctorName())
                .mostActiveDoctorTests(topDoctors.isEmpty() ? 0L : topDoctors.get(0).getTestsCount())
                .topDoctors(topDoctors)
                .averageTestsPerDay(totalTestResults > 0 ? totalTestResults / (double) ChronoUnit.DAYS.between(yearStart, today.plusDays(1)) : 0.0)
                .averageTestsPerWeek(testResultsThisYear / 52.0)
                .averageTestsPerMonth(testResultsThisYear / 12.0)
                .monthlyTrend("stable") // TODO: Calculate actual trend
                .monthlyGrowthRate(0.0) // TODO: Calculate actual growth rate
                .testCompletionRate(100.0) // TODO: Calculate based on visits vs test results
                .averageTestsPerVisit(0.0) // TODO: Calculate based on visits
                .build();
    }

    /**
     * Get pattern description
     */
    private String getPatternDescription(String pattern) {
        switch (pattern.toLowerCase()) {
            case "normal":
                return "Results within normal range";
            case "abnormal":
                return "Results outside normal parameters";
            case "elevated":
                return "Results above normal range";
            case "low":
                return "Results below normal range";
            default:
                return "Other result patterns";
        }
    }

    /**
     * Convert TestResult entity to TestResultResponse
     */
    private TestResultResponse convertToResponse(TestResult testResult, String message) {
        TestResultResponse.TestResultResponseBuilder builder = TestResultResponse.builder()
                .id(testResult.getId())
                .testType(testResult.getTestType())
                .result(testResult.getResult())
                .createdDate(testResult.getCreatedDate())
                .notes(testResult.getNotes())
                .message(message);

        // Visit information
        if (testResult.getVisit() != null) {
            Visit visit = testResult.getVisit();
            builder.visitId(visit.getId())
                   .visitDate(visit.getVisitDate())
                   .visitNotes(visit.getNotes());

            // Pet information
            if (visit.getPet() != null) {
                builder.petId(visit.getPet().getId())
                       .petName(visit.getPet().getName())
                       .petType(visit.getPet().getAnimalType() != null ?
                               visit.getPet().getAnimalType().getName() : "Unknown")
                       .petBreed(visit.getPet().getBreed());

                // Customer information
                if (visit.getPet().getCustomer() != null) {
                    builder.customerId(visit.getPet().getCustomer().getId())
                           .customerName(visit.getPet().getCustomer().getFullname())
                           .customerEmail(visit.getPet().getCustomer().getEmail())
                           .customerPhone(visit.getPet().getCustomer().getPhone());
                }
            }

            // Doctor information
            if (visit.getWorkSchedule().getDoctor() != null) {
                builder.doctorId(visit.getWorkSchedule().getDoctor().getId())
                       .doctorName(visit.getWorkSchedule().getDoctor().getFullname())
                       .doctorSpecialization(visit.getWorkSchedule().getDoctor().getSpecialization());
            }
        }

        return builder.build();
    }
}

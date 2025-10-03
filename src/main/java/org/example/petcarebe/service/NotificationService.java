package org.example.petcarebe.service;

import org.example.petcarebe.dto.request.notification.CreateNotificationRequest;
import org.example.petcarebe.dto.request.notification.UpdateNotificationStatusRequest;
import org.example.petcarebe.dto.response.notification.NotificationResponse;
import org.example.petcarebe.model.Customer;
import org.example.petcarebe.model.Notification;
import org.example.petcarebe.model.VaccineSchedule;
import org.example.petcarebe.repository.CustomerRepository;
import org.example.petcarebe.repository.NotificationRepository;
import org.example.petcarebe.repository.VaccineScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VaccineScheduleRepository vaccineScheduleRepository;

    /**
     * Create a new notification
     */
    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        // Validate customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + request.getCustomerId()));

        // Validate vaccine schedule if provided
        VaccineSchedule vaccineSchedule = null;
        if (request.getVaccineScheduleId() != null) {
            vaccineSchedule = vaccineScheduleRepository.findById(request.getVaccineScheduleId())
                    .orElseThrow(() -> new RuntimeException("Vaccine schedule not found with id: " + request.getVaccineScheduleId()));
        }

        // Create notification
        Notification notification = new Notification();
        notification.setMessage(request.getMessage());
        notification.setType(request.getType().toUpperCase());
        notification.setCreatedDate(LocalDate.now());
        notification.setStatus("UNREAD");
        notification.setCustomer(customer);
        notification.setVaccineSchedule(vaccineSchedule);

        Notification savedNotification = notificationRepository.save(notification);
        return convertToResponse(savedNotification);
    }

    /**
     * Get notification by ID
     */
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return convertToResponse(notification);
    }

    /**
     * Get all notifications
     */
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get notifications by customer ID
     */
    public List<NotificationResponse> getNotificationsByCustomerId(Long customerId) {
        return notificationRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get notifications by customer ID with pagination
     */
    public Page<NotificationResponse> getNotificationsByCustomerId(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Notification> notifications = notificationRepository.findByCustomerId(customerId, pageable);
        return notifications.map(this::convertToResponse);
    }

    /**
     * Get unread notifications for customer
     */
    public List<NotificationResponse> getUnreadNotifications(Long customerId) {
        return notificationRepository.findByCustomerIdAndStatus(customerId, "UNREAD").stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get notification count for customer
     */
    public Long getUnreadNotificationCount(Long customerId) {
        return notificationRepository.countUnreadByCustomerId(customerId);
    }

    /**
     * Update notification status
     */
    @Transactional
    public NotificationResponse updateNotificationStatus(Long id, UpdateNotificationStatusRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        notification.setStatus(request.getStatus().toUpperCase());
        Notification updatedNotification = notificationRepository.save(notification);
        return convertToResponse(updatedNotification);
    }

    /**
     * Mark notification as read
     */
    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        notification.setStatus("READ");
        Notification updatedNotification = notificationRepository.save(notification);
        return convertToResponse(updatedNotification);
    }

    /**
     * Mark all notifications as read for a customer
     */
    @Transactional
    public void markAllAsReadForCustomer(Long customerId) {
        List<Notification> unreadNotifications = notificationRepository.findByCustomerIdAndStatus(customerId, "UNREAD");
        for (Notification notification : unreadNotifications) {
            notification.setStatus("READ");
        }
        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Delete notification
     */
    @Transactional
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }

    /**
     * Get notifications by type
     */
    public List<NotificationResponse> getNotificationsByType(String type) {
        return notificationRepository.findByType(type.toUpperCase()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get notifications by customer and type
     */
    public List<NotificationResponse> getNotificationsByCustomerAndType(Long customerId, String type) {
        return notificationRepository.findByCustomerIdAndType(customerId, type.toUpperCase()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get recent notifications for customer
     */
    public List<NotificationResponse> getRecentNotifications(Long customerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return notificationRepository.findRecentByCustomerId(customerId, pageable).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ==================== HELPER METHODS ====================

    private NotificationResponse convertToResponse(Notification notification) {
        NotificationResponse.NotificationResponseBuilder builder = NotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .type(notification.getType())
                .createdDate(notification.getCreatedDate())
                .status(notification.getStatus());

        // Add customer information
//        if (notification.getCustomer() != null) {
//            builder.customerId(notification.getCustomer().getId())
//                    .customerName(notification.getCustomer().getFullname())
//                    .customerEmail(notification.getCustomer().getEmail());
//        }
//
//        // Add vaccine schedule information if available
//        if (notification.getVaccineSchedule() != null) {
//            builder.vaccineScheduleId(notification.getVaccineSchedule().getId())
//                    .vaccineScheduleDetails("Vaccine schedule for " + notification.getVaccineSchedule().getScheduledDate());
//        }

        return builder.build();
    }
}


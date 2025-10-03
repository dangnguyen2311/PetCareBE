package org.example.petcarebe.repository;

import org.example.petcarebe.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find notifications by customer
    List<Notification> findByCustomerId(Long customerId);

    // Find notifications by customer with pagination
    Page<Notification> findByCustomerId(Long customerId, Pageable pageable);

    // Find notifications by customer and status
    List<Notification> findByCustomerIdAndStatus(Long customerId, String status);

    // Find notifications by type
    List<Notification> findByType(String type);

    // Find notifications by status
    List<Notification> findByStatus(String status);

    // Find notifications by date range
    List<Notification> findByCreatedDateBetween(LocalDate fromDate, LocalDate toDate);

    // Count unread notifications for a customer
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.customer.id = :customerId AND n.status = 'UNREAD'")
    Long countUnreadByCustomerId(@Param("customerId") Long customerId);

    // Find recent notifications for a customer
    @Query("SELECT n FROM Notification n WHERE n.customer.id = :customerId ORDER BY n.createdDate DESC")
    List<Notification> findRecentByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

    // Find notifications by customer and type
    List<Notification> findByCustomerIdAndType(Long customerId, String type);
}


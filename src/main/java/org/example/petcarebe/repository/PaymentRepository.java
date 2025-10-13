package org.example.petcarebe.repository;

import org.example.petcarebe.enums.PaymentStatus;
import org.example.petcarebe.model.Invoice;
import org.example.petcarebe.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find by transaction code
    Optional<Payment> findByTransactionCode(String transactionCode);

    // Find payments by invoice
    List<Payment> findByInvoiceId(Long invoiceId);

    // Find payments by status
    List<Payment> findByStatus(PaymentStatus status);

    // Find payments by method
    List<Payment> findByMethod(String method);

    // Find payments by date range
    List<Payment> findByPaymentDateBetween(LocalDate fromDate, LocalDate toDate);

    // Find payments by customer (through invoice)
    @Query("SELECT p FROM Payment p JOIN p.invoice i JOIN i.customer c WHERE c.id = :customerId")
    List<Payment> findByCustomerId(@Param("customerId") Long customerId);

    // Search payments with multiple criteria
    @Query("SELECT p FROM Payment p WHERE " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:method IS NULL OR p.method = :method) AND " +
           "(:transactionCode IS NULL OR p.transactionCode LIKE %:transactionCode%) AND " +
           "(:invoiceId IS NULL OR p.invoice.id = :invoiceId) AND " +
           "(:fromDate IS NULL OR p.paymentDate >= :fromDate) AND " +
           "(:toDate IS NULL OR p.paymentDate <= :toDate) AND " +
           "(:minAmount IS NULL OR p.amount >= :minAmount) AND " +
           "(:maxAmount IS NULL OR p.amount <= :maxAmount)")
    Page<Payment> searchPayments(
            @Param("status") PaymentStatus status,
            @Param("method") String method,
            @Param("transactionCode") String transactionCode,
            @Param("invoiceId") Long invoiceId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            Pageable pageable
    );

    // Statistics queries
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    Long countByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    Double sumAmountByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status AND p.paymentDate BETWEEN :fromDate AND :toDate")
    Double sumAmountByStatusAndDateRange(
            @Param("status") PaymentStatus status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Query("SELECT p.method, COUNT(p) FROM Payment p GROUP BY p.method")
    List<Object[]> countByMethod();

    @Query("SELECT p.method, SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS' GROUP BY p.method")
    List<Object[]> sumAmountByMethod();

    @Query("SELECT p.paymentDate, SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS' AND p.paymentDate BETWEEN :fromDate AND :toDate GROUP BY p.paymentDate ORDER BY p.paymentDate")
    List<Object[]> getDailyRevenue(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query("SELECT p.paymentDate, COUNT(p) FROM Payment p WHERE p.paymentDate BETWEEN :fromDate AND :toDate GROUP BY p.paymentDate ORDER BY p.paymentDate")
    List<Object[]> getDailyTransactionCount(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    Optional<Payment> findByInvoice(Invoice invoice);

    List<Payment> findAllByInvoice(Invoice invoice);
}


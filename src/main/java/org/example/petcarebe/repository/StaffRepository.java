package org.example.petcarebe.repository;

import org.example.petcarebe.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    /**
     * Find staff by email
     */
    Optional<Staff> findByEmail(String email);

    /**
     * Find staff by position containing (case insensitive)
     */
    List<Staff> findByPositionContainingIgnoreCase(String position);

    /**
     * Find all active staff (user not deleted)
     */
    @Query("SELECT s FROM Staff s WHERE s.user.isDeleted = false")
    List<Staff> findByUser_IsDeletedFalse();

    /**
     * Find staff by user ID
     */
    Optional<Staff> findByUserId(Long userId);

    /**
     * Count active staff
     */
    @Query("SELECT COUNT(s) FROM Staff s WHERE s.user.isDeleted = false")
    Long countActiveStaff();

    /**
     * Find staff by fullname containing (case insensitive)
     */
    List<Staff> findByFullnameContainingIgnoreCase(String fullname);

    /**
     * Search staff by keyword (name, position, email)
     */
    @Query("SELECT s FROM Staff s WHERE s.user.isDeleted = false AND " +
           "(LOWER(s.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.position) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Staff> searchByKeyword(@Param("keyword") String keyword);

    /**
     * Find all staff ordered by fullname
     */
    List<Staff> findAllByOrderByFullnameAsc();
}


package org.example.petcarebe.repository;

import org.example.petcarebe.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    /**
     * Find disease by name (case insensitive)
     */
    Optional<Disease> findByNameIgnoreCase(String name);

    /**
     * Find diseases by name containing (case insensitive)
     */
    List<Disease> findByNameContainingIgnoreCase(String name);

    /**
     * Find diseases by description containing (case insensitive)
     */
    List<Disease> findByDescriptionContainingIgnoreCase(String description);

    /**
     * Search diseases by name or description
     */
    @Query("SELECT d FROM Disease d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Disease> searchByKeyword(@Param("keyword") String keyword);

    /**
     * Count total diseases
     */
    @Query("SELECT COUNT(d) FROM Disease d")
    Long countTotalDiseases();

    /**
     * Find diseases ordered by name
     */
    List<Disease> findAllByOrderByNameAsc();
}


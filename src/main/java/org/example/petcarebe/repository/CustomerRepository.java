package org.example.petcarebe.repository;

import org.example.petcarebe.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.status = :status")
    List<Customer> findAllByStatus(@Param("status") String status);

    Boolean existsCustomerByEmail(String email);

    Boolean existsCustomerByPhone(String phone);
}


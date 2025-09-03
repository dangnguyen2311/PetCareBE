package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Customer")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_date", nullable = false)
    private java.time.LocalDate createdDate;

    @Column(nullable = false)
    private String status;

    @OneToOne
    @JoinColumn(name = "Userid", referencedColumnName = "id")
    private User user;
}

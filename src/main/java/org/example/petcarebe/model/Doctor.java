package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "Doctor")
@Data
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @OneToOne
    @JoinColumn(name = "UserId", referencedColumnName = "id")
    private User user;
}
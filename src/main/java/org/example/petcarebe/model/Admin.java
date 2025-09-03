package org.example.petcarebe.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String gender;

    @OneToOne
    @JoinColumn(name = "Userid", referencedColumnName = "id")
    private User user;
}

package org.example.petcarebe.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String gender;

    @OneToOne
    @JoinColumn(name = "UserId", referencedColumnName = "id")
    private User user;
}

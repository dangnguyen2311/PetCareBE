package org.example.petcarebe.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Disease")
@Data
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

}

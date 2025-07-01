package org.example.hospital_admission_project.entity;


import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    @ManyToOne
    private FileStorage fileStorage;
    @ManyToOne
    private Category category;
    private String drugUnit;
    private Double rating;
}

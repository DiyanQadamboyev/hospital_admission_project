package org.example.hospital_admission_project.entity;
import jakarta.persistence.*;
import lombok.*;



@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Doctor doctor;
}
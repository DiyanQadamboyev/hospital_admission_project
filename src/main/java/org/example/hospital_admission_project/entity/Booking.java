package org.example.hospital_admission_project.entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.hospital_admission_project.entity.enums.ConsultationStatus;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer doctorId;
    private String reason;
    private String date;
    private String availableTimes;
    @Enumerated(EnumType.STRING)
    private ConsultationStatus status;
    @ManyToOne
    private Consultation consultation;




}

package org.example.hospital_admission_project.entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.hospital_admission_project.entity.enums.ConsultationStatus;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long doctorId;
    private String reason;
    private String date;
    private String availableTimes;
    @Enumerated(EnumType.STRING)
    private ConsultationStatus status;
    @ManyToOne
    private Consultation consultation;




}

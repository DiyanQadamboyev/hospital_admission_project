package org.example.hospital_admission_project.entity;
import jakarta.persistence.*;
import lombok.*;
import org.example.hospital_admission_project.entity.enums.WorkTime;



@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Doctor doctor;
    @Enumerated(EnumType.STRING)
    private WorkTime workTime;
}
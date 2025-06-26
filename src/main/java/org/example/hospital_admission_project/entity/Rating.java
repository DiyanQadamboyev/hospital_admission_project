package org.example.hospital_admission_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    private List<Long> usersId;
    private Long ownerId;
    private Double ownerRating;
    @ElementCollection
    private List<Double> ratings = new ArrayList<>();
}


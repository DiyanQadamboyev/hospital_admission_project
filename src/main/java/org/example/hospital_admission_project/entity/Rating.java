package org.example.hospital_admission_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ElementCollection
    private List<Double> ratings = new ArrayList<>();
    @ElementCollection
    private List<Integer> usersId = new ArrayList<>();
    private Integer ownerId;
    private Double ownerRating;

}


package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Optional<Rating> findByOwnerId(Integer doctorId);
}
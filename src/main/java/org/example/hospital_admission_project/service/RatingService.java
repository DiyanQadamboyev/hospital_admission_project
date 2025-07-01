package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Rating;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {
    public Double ratingService(Rating rating1) {
        Double sum = 0.0;
        for (Double ratingRating : rating1.getRatings()) {
            sum += ratingRating;
        }
        return sum / rating1.getRatings().size();
    }
}

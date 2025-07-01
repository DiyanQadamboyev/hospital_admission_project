package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Expert;
import org.example.hospital_admission_project.repo.ExpertRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpertService {
    private final ExpertRepository expertRepository;
    public Expert findExpertName(String text) {

    }

    public Expert getId(Integer expertId) {
        if (expertRepository.findById(expertId).isEmpty()) {
            return null;
        }
        return expertRepository.findById(expertId).get();
    }
}

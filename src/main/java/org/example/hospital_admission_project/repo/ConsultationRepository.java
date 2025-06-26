package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {
}
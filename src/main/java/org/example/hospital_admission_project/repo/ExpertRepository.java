package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpertRepository extends JpaRepository<Expert, Integer> {
}
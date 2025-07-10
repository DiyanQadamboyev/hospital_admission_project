package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Integer> {
    Optional<Expert> findByName(String name);
}
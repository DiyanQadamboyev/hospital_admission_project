package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findByEmail(String email);

    List<Doctor> findByExpert_Id(Integer id);

    Optional<Doctor> findByName(String name);

    List<Doctor> findByRatingBetween(double v, double v1);
}
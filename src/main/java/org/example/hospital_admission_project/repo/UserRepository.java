package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String attr0);
}
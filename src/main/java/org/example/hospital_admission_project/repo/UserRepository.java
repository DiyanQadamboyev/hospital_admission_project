package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String attr0);

    Optional<User> findByEmail(String email);
}
package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {
    Optional<ConfirmationCode> findByEmail(String email);

    Optional<ConfirmationCode> findByEmailAndCode(String email, String code);

    void deleteByEmail(String email);
}
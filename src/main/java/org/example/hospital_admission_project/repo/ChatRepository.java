package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
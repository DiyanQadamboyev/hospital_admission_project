package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
    List<Chat> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
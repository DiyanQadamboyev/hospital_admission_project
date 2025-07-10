package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findBySenderIdOrReceiverId(Integer id, Integer id1);

    List<Chat> findBySenderIdAndReceiverId(Integer id, Integer doctorId);

    List<Chat> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(Integer id, Integer doctorId, Integer doctorId1, Integer id1);
}
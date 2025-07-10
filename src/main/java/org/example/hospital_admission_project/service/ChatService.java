package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Chat;
import org.example.hospital_admission_project.entity.Doctor;
import org.example.hospital_admission_project.entity.User;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.repo.ChatRepository;
import org.example.hospital_admission_project.repo.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final DoctorRepository doctorRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    public ResponseEntity<?> sendMessageDoctor(Integer doctorId, String message) {
        User sender = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Doctor receiver = doctorRepository.findById(doctorId)
                .orElse(null);

        if (receiver == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SendMessage(false, "Bunday doktor topilmadi", null));
        }
         Chat chatMessage = new Chat();
        chatMessage.setSenderId(sender.getId());
        chatMessage.setReceiverId(receiver.getId());
        chatMessage.setMessage(message);
        chatMessage.setDateTime(LocalDateTime.now());

        chatRepository.save(chatMessage);
        messagingTemplate.convertAndSend("/topic/chat/doctor/" + receiver.getId(), chatMessage);
        messagingTemplate.convertAndSend("/topic/chat/user/" + sender.getId(), chatMessage);

        return ResponseEntity.ok(new SendMessage(true, "Xabar muvaffaqiyatli jo'natildi", chatMessage));
    }

    public ResponseEntity<List<Chat>> getAllMessagesForUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Chat> messages = chatRepository.findBySenderIdOrReceiverId(user.getId(), user.getId());
        return ResponseEntity.ok(messages);
    }

    public ResponseEntity<List<Chat>> getMessagesWithDoctor(Integer doctorId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Chat> messages = chatRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                user.getId(), doctorId, doctorId, user.getId()
        );
        return ResponseEntity.ok(messages);
    }
}

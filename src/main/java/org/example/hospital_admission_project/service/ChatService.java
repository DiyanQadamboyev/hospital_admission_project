package org.example.hospital_admission_project.service;

import org.example.hospital_admission_project.config.JwtAuthenticatorFilter;
import org.example.hospital_admission_project.entity.Chat;
import org.example.hospital_admission_project.entity.Doctor;
import org.example.hospital_admission_project.entity.User;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.repo.ChatRepository;
import org.example.hospital_admission_project.repo.DoctorRepository;
import org.example.hospital_admission_project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {


    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private  SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtAuthenticatorFilter authenticatorFilter;

    @Autowired
    private DoctorRepository doctorRepository;

    public ResponseEntity<SendMessage> sendMessageDoctor(Long id, String message) {
        User senderId = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Doctor> receiverId = doctorRepository.findById(id);
        if (!receiverId.isPresent()) {
            SendMessage sendMessage = new SendMessage(false, "Bunday foydalanuvchi yoq", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(sendMessage);
        }
        Chat chatMessage = new Chat();
        chatMessage.setSenderId(senderId.getId());
        chatMessage.setReceiverId(receiverId.get().getId());
        chatMessage.setMessage(message);
        chatMessage.setDateTime(LocalDateTime.now());
        chatRepository.save(chatMessage);
        messagingTemplate.convertAndSend("/topic/chat", chatMessage);
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Xabar jo'natildi", chatMessage));
    }

    public ResponseEntity<List<Chat>> getAllMessagesForUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Chat> messages = chatRepository.findBySenderIdOrReceiverId(user.getId(), user.getId());
        return ResponseEntity.ok(messages);
    }

    public ResponseEntity<List<Chat>> getMessagesWithDoctor(Long doctorId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Chat> messages = chatRepository.findBySenderIdAndReceiverId(user.getId(), doctorId);
        return ResponseEntity.ok(messages);
    }
}

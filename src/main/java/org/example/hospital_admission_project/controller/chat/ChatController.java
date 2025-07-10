package org.example.hospital_admission_project.controller.chat;


import org.example.hospital_admission_project.entity.Chat;
import org.example.hospital_admission_project.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatController {

    @Autowired
    ChatService chatService;

    @PostMapping("/send/message/doctor")
    public ResponseEntity<?> sendMessageDoctor(@RequestParam Long id, @RequestBody String message) {
        return chatService.sendMessageDoctor((long) Math.toIntExact(id), message);
    }

    @GetMapping("/user/messages")
    public ResponseEntity<List<Chat>> getAllMessagesForUser() {
        return chatService.getAllMessagesForUser();
    }

    @GetMapping("/doctor/{doctorId}/messages")
    public ResponseEntity<List<Chat>> getMessagesWithDoctor(@PathVariable Long doctorId) {
        return chatService.getMessagesWithDoctor(doctorId);
    }
}
package org.example.hospital_admission_project.controller;

import lombok.AllArgsConstructor;
import org.example.hospital_admission_project.entity.Chat;
import org.example.hospital_admission_project.service.ChatService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_PATH+ApiConstants.API_VERSION)
@AllArgsConstructor
public class ChatController {
   private final ChatService chatService;

    @PostMapping("/send/message/doctor")
    public ResponseEntity<?> sendMessageDoctor(@RequestParam Integer id, @RequestBody String message) {
        return chatService.sendMessageDoctor(id, message);
    }

    @GetMapping("/user/messages")
    public ResponseEntity<List<Chat>> getAllMessagesForUser() {
        return chatService.getAllMessagesForUser();
    }

    @GetMapping("/doctor/{doctorId}/messages")
    public ResponseEntity<List<Chat>> getMessagesWithDoctor(@PathVariable Integer doctorId) {
        return chatService.getMessagesWithDoctor(doctorId);
    }
}

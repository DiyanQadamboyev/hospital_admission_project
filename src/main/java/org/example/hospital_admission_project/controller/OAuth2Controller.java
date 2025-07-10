package org.example.hospital_admission_project.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final GoogleTokenValidator googleTokenValidator;
    private final AuthServiceImpl authService;

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SendMessage(false, "Authorization header is missing or invalid format", null));
        }

        String idTokenString = authHeader.substring(7);
        GoogleIdToken.Payload payload = googleTokenValidator.validateToken(idTokenString);
        if (payload == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new SendMessage(false, "Invalid or expired token", null));
        }

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", payload.getEmail());
        userInfo.put("name", (String) payload.get("name"));
        return authService.processUser(userInfo);
    }
}


package org.example.hospital_admission_project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.payload.LoginDto;
import org.example.hospital_admission_project.payload.UserDto;
import org.example.hospital_admission_project.service.AuthService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.example.hospital_admission_project.utils.ApiConstants.API_PATH;

@RestController
@RequestMapping(ApiConstants.API_PATH + ApiConstants.API_VERSION)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final HttpSession session;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        return authService.signUp(userDto);
    }
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
            return authService.login(loginDto);
        }
    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestParam String email) {
        return authService.forgetPassword(session, email);
    }
    @PostMapping("/check-code")
    public ResponseEntity<?> checkCode(@RequestParam String code) {
        return authService.checkCode(session, code);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String newPassword, @RequestParam String confirmPassword) {
        return authService.resetPassword(session, newPassword, confirmPassword);
    }

    }

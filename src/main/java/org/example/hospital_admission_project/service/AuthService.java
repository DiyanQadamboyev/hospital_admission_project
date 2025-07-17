package org.example.hospital_admission_project.service;

import jakarta.servlet.http.HttpSession;
import org.example.hospital_admission_project.payload.LoginDto;
import org.example.hospital_admission_project.payload.UserDto;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    ResponseEntity<?> signUp(UserDto userDto);

    ResponseEntity<?> login(LoginDto loginDto);

    ResponseEntity<?> forgetPassword(HttpSession session, String email);

    ResponseEntity<?> resetPassword(HttpSession session, String newPassword, String confirmPassword);

    ResponseEntity<?> checkCode(HttpSession email, String code);
}

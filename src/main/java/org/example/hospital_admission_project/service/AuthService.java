package org.example.hospital_admission_project.service;

import org.example.hospital_admission_project.payload.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface AuthService {
    ResponseEntity<?> signUp(UserDto userDto);
}

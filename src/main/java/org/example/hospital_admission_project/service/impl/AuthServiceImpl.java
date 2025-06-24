package org.example.hospital_admission_project.service.impl;

import org.example.hospital_admission_project.entity.User;
import org.example.hospital_admission_project.entity.enums.UserRoles;
import org.example.hospital_admission_project.payload.UserDto;
import org.example.hospital_admission_project.repo.UserRepository;
import org.example.hospital_admission_project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;


    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> signUp(UserDto userDto) {
        if (userRepository.existsByEmail((userDto.getEmail()))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
       user.setUserRole(UserRoles.USER);
        user.setPassword(userDto.getPassword());//Password encode qilinishi mumkin
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    }


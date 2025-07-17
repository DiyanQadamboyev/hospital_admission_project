package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Expert;
import org.example.hospital_admission_project.entity.enums.Role;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.payload.ExpertDTO;
import org.example.hospital_admission_project.repo.ExpertRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertService {
    private final ExpertRepository expertRepository;
    private final UserService userService;

    public Expert findExpertName(String name) {
        Optional<Expert> optionalExpert = expertRepository.findByName(name);
        return optionalExpert.orElse(null);
    }

    public Expert getId(Integer expertId) {
        if (expertRepository.findById(expertId).isEmpty()) {
            return null;
        }
        return expertRepository.findById(expertId).get();
    }

    public ResponseEntity<?> getAll() {
        List<Expert> all = expertRepository.findAll();
        if (all.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Expert list empty!", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "All Expert! ", all));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(ExpertDTO dto) {
        Expert expert = new Expert();
        if (dto.getName() == null || dto.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Name is required!", dto));
        }
        if (expertRepository.findByName(dto.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SendMessage(false, "Expert already exist!", dto));
        }
        expert.setName(dto.getName());
        expertRepository.save(expert);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessage(true, "Expert successfully saved!", expert));
    }

    public ResponseEntity<?> getExpertId(Integer id) {
        Optional<Expert> optionalExpert = expertRepository.findById(id);
        if (optionalExpert.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Expert not found!", id));
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalExpert.get());
    }

    public ResponseEntity<?> getExpertName(String name) {
        Optional<Expert> byName = expertRepository.findByName(name);
        if (byName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Expert not found!", name));
        }
        return ResponseEntity.status(HttpStatus.OK).body(byName.get());
    }
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(Integer id, ExpertDTO dto) {
        Optional<Expert> byId = expertRepository.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Expert not found!", id));
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Name is required!", dto.getName()));
        }
        Expert expert = byId.get();
        expert.setName(dto.getName());
        expertRepository.save(expert);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessage(true, "Expert successfully updated!", expert));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(Integer id) {
        expertRepository.findById(id).ifPresent(expertRepository::delete);
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Expert successfully deleted!", id));
    }
}

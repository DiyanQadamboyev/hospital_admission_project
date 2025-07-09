package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Doctor;
import org.example.hospital_admission_project.payload.DoctorDto;
import org.example.hospital_admission_project.service.DoctorService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.hospital_admission_project.utils.ApiConstants.API_PATH;
import static org.example.hospital_admission_project.utils.ApiConstants.API_VERSION;

@RestController
@RequestMapping(API_PATH + API_VERSION)
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService service;

   @GetMapping
   public ResponseEntity<?> getAllDoctors() {
       return service.getAll();
   }

    @PostMapping("/add")
    public ResponseEntity<?> addDoctors(@RequestBody DoctorDto doctorDto) {
        return service.save(doctorDto);
    }

    @PostMapping("/add/rating")
    public ResponseEntity<?> addRating(@RequestParam Long DoctorId, @RequestParam Double Rating) {
        return service.addRating(DoctorId,Rating);
    }

    @GetMapping("/find-by-id")
    public ResponseEntity<?> getDoctorId(@RequestParam Long id) {
        return service.getDoctorId(id);
    }

    @GetMapping("/find-by-name")
    public ResponseEntity<?> getDoctorName(@RequestParam String name) {
        return service.getDoctorName(name);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id, @RequestBody DoctorDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        return service.delete(id);
    }
}

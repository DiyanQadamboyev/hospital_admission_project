package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.service.DoctorDetailsService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.API_PATH+ApiConstants.API_VERSION)
@RequiredArgsConstructor
public class DoctorDetailsController {
    private final DoctorDetailsService doctorDetailsService;

    @PostMapping("/bookAppointment")
    public ResponseEntity<?> bookAppointment(@RequestParam String date, @RequestParam String time, @RequestParam Long doctorId) {
        return doctorDetailsService.bookAppointment(date, time, doctorId);
    }
    @PostMapping("/findDoctorbyId")
    public ResponseEntity<?> findDoctorbyId(@RequestParam Long doctorId) {
        return doctorDetailsService.findDoctorbyId(doctorId);
    }
}

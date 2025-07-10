package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.service.AppointmentService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_PATH+ApiConstants.API_VERSION+"/user/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/booking")
    public ResponseEntity<?> createBooking(@RequestParam Long doctorId, @RequestParam String reason) {
        return appointmentService.createBooking(doctorId, reason);
    }

    @GetMapping("/all-appointments")
    public ResponseEntity<?> getUserBookings() {
        return appointmentService.getUserBookings();
    }
    @GetMapping("/canceled-appointments")
    public ResponseEntity<?> getUserCanceledBookings() {
        return appointmentService.getUserCanceledBookings();
    }
    @GetMapping("/upcoming-appointments")
    public ResponseEntity<?> getUserUpcomingBookings() {
        return appointmentService.getUserUpcomingBookings();
    }
    @GetMapping("/completed-appointments")
    public ResponseEntity<?> getUserCompletedBookings() {
        return appointmentService.getUserCompletedBookings();
    }

    @PostMapping("/cancelBooking")
    public ResponseEntity<?> cancelBooking(@RequestParam Integer bookingId) {
        return appointmentService.cancelBooking(bookingId);
    }
}

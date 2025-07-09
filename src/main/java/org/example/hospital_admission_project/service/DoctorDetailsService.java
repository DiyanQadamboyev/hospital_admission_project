package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Booking;
import org.example.hospital_admission_project.entity.Doctor;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.repo.BookingRepository;
import org.example.hospital_admission_project.repo.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorDetailsService {
    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    public ResponseEntity<?> bookAppointment(String date, String time, Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Doctor not found", null));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate inputDate = LocalDate.parse(date, formatter);
        if (inputDate.isBefore(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Date is after start date", inputDate));
        }

        for (String availableTime : optionalDoctor.get().getAvailableTimes()) {
            if (availableTime.equals(time)) {
                Booking booking = new Booking();
                booking.setDate(date);
                booking.setDoctorId(Long.valueOf(doctorId));
                booking.setAvailableTimes(time);
                booking.setUserId(userService.getUser().getId());
                bookingRepository.save(booking);
                return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessage(true, "success!", booking));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Time is not available", inputDate));


    }

    public ResponseEntity<?> findDoctorbyId(Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        return optionalDoctor.map(doctor -> ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Doctor found!", doctor))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Doctor not found", null)));

    }
}

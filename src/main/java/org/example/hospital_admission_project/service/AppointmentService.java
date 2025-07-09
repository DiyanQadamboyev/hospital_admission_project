package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Booking;
import org.example.hospital_admission_project.entity.Consultation;
import org.example.hospital_admission_project.entity.Doctor;
import org.example.hospital_admission_project.entity.User;
import org.example.hospital_admission_project.entity.enums.ConsultationStatus;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.repo.BookingRepository;
import org.example.hospital_admission_project.repo.ConsultationRepository;
import org.example.hospital_admission_project.repo.DoctorRepository;
import org.example.hospital_admission_project.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final DoctorRepository doctorRepository;
    private final ConsultationRepository consultationRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    public ResponseEntity<?> createBooking(Long doctorId, String reason) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Doctor not found", null));
        }

        Doctor doctor = optionalDoctor.get();
        Consultation consultation = new Consultation();
        consultation.setDoctorId(Math.toIntExact(doctorId));
        consultation.setAdminPrice(1.0);
        consultation.setDiscount(0.0);
        consultation.setDoctorPrice(doctor.getConsultationPrice());
        consultation.setTotalPrice(consultation.getDoctorPrice() + consultation.getAdminPrice());
        consultationRepository.save(consultation);

        User user = userService.getUser();
        Optional<Booking> optionalBooking = bookingRepository.findByUserIdAndDoctorId(user.getId(), doctorId);
        if (optionalBooking.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Booking not found", null));
        }
        Booking booking = optionalBooking.get();
        booking.setConsultation(consultation);
        booking.setReason(reason);
        booking.setStatus(ConsultationStatus.UPCOMING);
        doctor.getAvailableTimes().remove(booking.getAvailableTimes());
        doctorRepository.save(doctor);
        bookingRepository.save(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessage(true, "Booking created", null));
    }

    public ResponseEntity<?> getUserBookings() {
        User user = userService.getUser();
        List<Booking> bookings = bookingRepository.findByUserId(user.getId());

        if (bookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "No bookings found", null));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "", bookings));
    }

    public ResponseEntity<?> getUserCanceledBookings() {
        List<Booking> byStatus = bookingRepository.findByStatus(ConsultationStatus.CANCELED);
        if (byStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "No bookings found", null));
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(new SendMessage(true, "Canceled Booking Found!", byStatus));

    }

    public ResponseEntity<?> cancelBooking(Integer bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(Long.valueOf(bookingId));

        if (optionalBooking.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Booking not found", null));
        }

        Booking booking = optionalBooking.get();
        if (!booking.getStatus().equals(ConsultationStatus.UPCOMING)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Booking cannot be canceled", null));
        }
        Optional<Doctor> optionalDoctor = doctorRepository.findById(booking.getDoctorId());
        optionalDoctor.get().getAvailableTimes().add(booking.getAvailableTimes());
        booking.setStatus(ConsultationStatus.CANCELED);
        bookingRepository.save(booking);

        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Booking canceled successfully", null));
    }

    public ResponseEntity<?> getUserUpcomingBookings() {
        List<Booking> byStatus = bookingRepository.findByStatus(ConsultationStatus.UPCOMING);
        if (byStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "No bookings found", null));
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(new SendMessage(true, "Upcoming Booking Found!", byStatus));

    }

    public ResponseEntity<?> getUserCompletedBookings() {
        List<Booking> byStatus = bookingRepository.findByStatus(ConsultationStatus.CANCELED);
        if (byStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "No bookings found", null));
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(new SendMessage(true, "Completed Booking Found!", byStatus));

    }
}

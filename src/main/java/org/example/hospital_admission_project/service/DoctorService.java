package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Attachment;
import org.example.hospital_admission_project.entity.Doctor;
import org.example.hospital_admission_project.entity.Expert;
import org.example.hospital_admission_project.entity.Rating;
import org.example.hospital_admission_project.entity.enums.Role;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.payload.DoctorDto;
import org.example.hospital_admission_project.repo.AttachmentRepository;
import org.example.hospital_admission_project.repo.DoctorRepository;
import org.example.hospital_admission_project.repo.RatingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final ExpertService expertService;
    private final RatingRepository ratingRepository;
    private final RatingService ratingService;
    private final AttachmentRepository attachmentRepository;
    public Doctor findDoctorName(String name) {
        Optional<Doctor> optionalDoctor = doctorRepository.findByName(name);
        return optionalDoctor.orElse(null);
    }

    public List<Doctor> findByRating() {
        return doctorRepository.findByRatingBetween(3.9, 5.0);
    }

    public ResponseEntity<?> getAll() {
        List<Doctor> all = doctorRepository.findAll();
        if (all.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SendMessage(false, "doctor list empty!", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "all doctors!", all));
    }

    public ResponseEntity<?> save(DoctorDto doctorDto) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", null));
        }
        Expert expert = expertService.getId(doctorDto.getExpertId());
        if (expert == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Expert not found!", doctorDto.getExpertId()));
        }
        if (doctorDto.getName() == null | doctorDto.getName().isBlank() | doctorDto.getPassword() == null | doctorDto.getPassword().isBlank() | doctorDto.getLocation() == null | doctorDto.getLocation().isBlank() | doctorDto.getImageUrl() == null | doctorDto.getImageUrl().isBlank() | doctorDto.getPhoneNumber() == null | doctorDto.getPhoneNumber().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, " Name or password or location or image url or phone number is incorrect! ", doctorDto));
        }
        Optional<Doctor> byEmail = doctorRepository.findByEmail(doctorDto.getEmail());
        if (byEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SendMessage(false, " Doctor already exists!", doctorDto.getEmail()));
        }

        Doctor doctor = new Doctor();
        doctorRepository.save(doctor);
        doctor.setName(doctorDto.getName());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setPassword(doctorDto.getPassword());
        doctor.setPhoneNumber(doctorDto.getPhoneNumber());
        doctor.setRole(Role.DOCTOR);
        doctor.setConsultationPrice(doctorDto.getConsultationPrice());
        doctor.setExpert(expert);

        Rating rating = new Rating();
        ratingRepository.save(rating);
        rating.setOwnerId(doctor.getId());
        rating.getRatings().add(0.0);
        rating.getUsersId().add(userService.getUser().getId());
        rating.setOwnerRating(0.0);
        ratingRepository.save(rating);
        doctor.setRating(0.0);
        Attachment attachment = attachmentRepository.findById(doctorDto.getAttachmentId()).orElse(null);
        doctor.setAttachment(attachment);
        doctor.setLocation(doctorDto.getLocation());
        doctorRepository.save(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessage(true, "Doctor successfully saved!", doctor));
    }

    public ResponseEntity<?> addRating(Long doctorId, Double rating) {

        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Doctor not found!", doctorId));
        }
        Doctor doctor = optionalDoctor.get();
        Optional<Rating> optionalRating = ratingRepository.findByOwnerId(doctorId);
        Rating rating1 = optionalRating.get();
        for (Long l : rating1.getUsersId()) {
            if (l.equals(userService.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new SendMessage(false, "Rating already exists!", rating1));
            }

        }
        if (rating > 0 | rating < 6) {
            rating1.getRatings().add(rating);
            rating1.getUsersId().add(userService.getUser().getId());
            rating1.setOwnerRating(ratingService.ratingService(rating1));
            ratingRepository.save(rating1);
            doctor.setRating(rating1.getOwnerRating());

            return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Doctor successfully add rating!", doctor));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "BAD Rating", rating));
    }

    public ResponseEntity<?> getDoctorId(Long id) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (optionalDoctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Doctor not found!", id));
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(optionalDoctor.get());
    }

    public ResponseEntity<?> getDoctorName(String name) {
        Optional<Doctor> optionalDoctor = doctorRepository.findByName(name);
        if (optionalDoctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Doctor not found!", name));
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(optionalDoctor.get());
    }

    public ResponseEntity<?> update(Long id, DoctorDto dto) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", null));
        }
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        Expert expert = expertService.getId(dto.getExpertId());

        if (optionalDoctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Doctor not found!", dto));
        }
        if (expert == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Expert not found!", dto.getExpertId()));
        }
        if (dto.getName() == null || dto.getName().isBlank() || dto.getPassword() == null || dto.getPassword().isBlank() || dto.getLocation() == null || dto.getLocation().isBlank() || dto.getAttachmentId() == null || dto.getAttachmentId().toString().isBlank() || dto.getPhoneNumber() == null || dto.getPhoneNumber().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, " Name or password or location or image url or phone number is incorrect! ", dto));
        }
        Doctor doctor = optionalDoctor.get();
        doctor.setName(dto.getName());
        doctor.setEmail(dto.getEmail());
        doctor.setPassword(dto.getPassword());
        doctor.setExpert(expert);
        Attachment attachment = attachmentRepository.findById(dto.getAttachmentId()).orElse(null);
        doctor.setAttachment(attachment);
        doctor.setLocation(dto.getLocation());
        doctor.setPhoneNumber(dto.getPhoneNumber());

        Optional<Rating> optionalRating = ratingRepository.findByOwnerId(doctor.getId());
        Rating rating = optionalRating.get();


        rating.setOwnerRating(ratingService.ratingService(rating));
        ratingRepository.save(rating);
        doctor.setRating(rating.getOwnerRating());
        doctor.setRole(Role.DOCTOR);
        doctorRepository.save(doctor);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SendMessage(true, "Doctor successfully update!", doctor));
    }

    public ResponseEntity<?> delete(Long id) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", null));
        }
        doctorRepository.findById(id).ifPresent(doctorRepository::delete);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SendMessage(true, "Doctor successfully delete!", id));
    }
}

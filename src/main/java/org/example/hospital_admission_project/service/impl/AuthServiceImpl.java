package org.example.hospital_admission_project.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.config.JwtAuthenticatorFilter;
import org.example.hospital_admission_project.config.JwtProvider;
import org.example.hospital_admission_project.entity.ConfirmationCode;
import org.example.hospital_admission_project.entity.Doctor;
import org.example.hospital_admission_project.entity.User;
import org.example.hospital_admission_project.entity.enums.Role;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.payload.LoginDto;
import org.example.hospital_admission_project.payload.UserDto;
import org.example.hospital_admission_project.repo.ConfirmationCodeRepository;
import org.example.hospital_admission_project.repo.DoctorRepository;
import org.example.hospital_admission_project.repo.UserRepository;
import org.example.hospital_admission_project.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final JwtProvider jwtProvider;
    private final JavaMailSender mailSender;
    private final JwtAuthenticatorFilter jwtAuthenticator;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationCodeRepository confirmationCodeRepository;

    @Override
    public ResponseEntity<?> signUp(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(SendMessage.failure("Email already exists"));
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setUserRole(Role.USER);
        user.setPassword(userDto.getPassword()); // Parolni encode qilish tavsiya etiladi
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SendMessage.success("Foydalanuvchi ro'yxatdan o'tdi", user));
    }

    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {
        Optional<User> userOpt = userRepository.findByEmail(loginDto.getEmail());
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(loginDto.getEmail());

        if (userOpt.isEmpty() && doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(SendMessage.failure("Email yoki parol noto‘g‘ri"));
        }

        if (userOpt.isPresent() && passwordEncoder.matches(loginDto.getPassword(), userOpt.get().getPassword())) {
            String token = jwtProvider.generateToken(loginDto.getEmail());
            jwtAuthenticator.setUserToContext(loginDto.getEmail());
            return ResponseEntity.ok(SendMessage.success("User logged in", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(SendMessage.failure("Invalid credentials"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> forgetPassword(HttpSession session, String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(SendMessage.failure("Foydalanuvchi topilmadi"));
        }

        session.setAttribute("email", email);
        String otpCode = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        Instant confirmationDate = Instant.now().plusSeconds(120);

        ConfirmationCode confirmationCode = confirmationCodeRepository.findByEmail(email)
                .orElse(new ConfirmationCode());

        if (confirmationCode.getConfirmationDate() != null &&
                confirmationCode.getConfirmationDate().isAfter(Instant.now())) {
            return ResponseEntity.badRequest()
                    .body(SendMessage.failure("Mavjud tasdiqlash kodi hali amal qiladi"));
        }

        confirmationCode.setEmail(email);
        confirmationCode.setCode(otpCode);
        confirmationCode.setConfirmationDate(confirmationDate);
        confirmationCodeRepository.save(confirmationCode);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Parolni yangilash uchun tasdiqlash kodi");
            message.setText("Sizning tasdiqlash kodingiz: " + otpCode);
            mailSender.send(message); // <<< JO'NATILAYAPTI
        } catch (MailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(SendMessage.failure("Email yuborishda xatolik"));
        }

        return ResponseEntity.ok(SendMessage.success("Tasdiqlash kodi yuborildi"));
    }


    @Override
    @Transactional
    public ResponseEntity<?> checkCode(HttpSession session, String code) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.badRequest()
                    .body(SendMessage.failure("Email topilmadi"));
        }

        Optional<ConfirmationCode> codeOpt = confirmationCodeRepository.findByEmailAndCode(email, code);
        if (codeOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(SendMessage.failure("Noto'g'ri yoki mavjud bo'lmagan tasdiqlash kodi"));
        }

        ConfirmationCode confirmationCode = codeOpt.get();
        if (confirmationCode.getConfirmationDate().isBefore(Instant.now())) {
            return ResponseEntity.badRequest()
                    .body(SendMessage.failure("Kod muddati o'tgan"));
        }

        session.setAttribute("codeVerified", true);
        confirmationCodeRepository.delete(confirmationCode);

        return ResponseEntity.ok(SendMessage.success("Kod muvaffaqiyatli kiritildi"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> resetPassword(HttpSession session, String newPassword, String confirmPassword) {
        String email = (String) session.getAttribute("email");
        Boolean isCodeVerified = (Boolean) session.getAttribute("codeVerified");

        if (email == null) {
            return ResponseEntity.badRequest()
                    .body(SendMessage.failure("Email topilmadi"));
        }

        if (isCodeVerified == null || !isCodeVerified) {
            return ResponseEntity.badRequest()
                    .body(SendMessage.failure("Tasdiqlash kodi tekshirilmagan"));
        }

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest()
                    .body(SendMessage.failure("Parollar bir-biriga mos kelmaydi"));
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        confirmationCodeRepository.deleteByEmail(email);
        session.removeAttribute("codeVerified");

        return ResponseEntity.ok(SendMessage.success("Parol muvaffaqiyatli yangilandi"));
    }
}

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
    private final JavaMailSender mailSender; // xatolik mavjud
    private final JwtAuthenticatorFilter jwtAuthenticator;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationCodeRepository confirmationCodeRepository;

    @Override
    public ResponseEntity<?> signUp(UserDto userDto) {
        if (userRepository.existsByEmail((userDto.getEmail()))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setUserRole(Role.USER);
        user.setPassword(userDto.getPassword());//Password encode qilinishi mumkin
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {
        Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
        Optional<Doctor> optionalDoctor = doctorRepository.findByEmail(loginDto.getEmail());
        if (user.isEmpty() & optionalDoctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email or password is incorrect");
        }

        if (passwordEncoder.matches(loginDto.getPassword(), user.get().getPassword())) {
            String token = jwtProvider.generateToken(loginDto.getEmail());
            jwtAuthenticator.setUserToContext(loginDto.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "User logged in", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SendMessage(false, "Invalid credentials", null));
    }

    @Override
    @Transactional
    public ResponseEntity<?> forgetPassword(HttpSession session, String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SendMessage(false, "Foydalanuvchi topilmadi", null));
        }

        session.setAttribute("email", email);

        String otpCode = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        Instant confirmationDate = Instant.now().plusSeconds(120); // Kod 2 minut amal qiladi

        Optional<ConfirmationCode> codeOpt = confirmationCodeRepository.findByEmail(email);
        ConfirmationCode confirmationCode;

        if (codeOpt.isPresent()) {
            confirmationCode = codeOpt.get();
            if (confirmationCode.getConfirmationDate().isAfter(Instant.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SendMessage(false, "Mavjud tasdiqlash kodi hali amal qiladi", null));
            }
            confirmationCode.setCode(otpCode);
            confirmationCode.setConfirmationDate(confirmationDate);
        } else {
            confirmationCode = new ConfirmationCode();
            confirmationCode.setEmail(email);
            confirmationCode.setCode(otpCode);
            confirmationCode.setConfirmationDate(confirmationDate);
        }

        confirmationCodeRepository.save(confirmationCode);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Parolni yangilash uchun tasdiqlash kodi");
            message.setText("Sizning tasdiqlash kodingiz: " + otpCode);
            mailSender.send(message);
        } catch (MailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SendMessage(false, "Email yuborishda xato", null));
        }

        return ResponseEntity.ok(new SendMessage(true, "Tasdiqlash kodi yuborildi", null));
    }

    @Override
    @Transactional
    public ResponseEntity<?> checkCode(HttpSession session, String code) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Email topilmadi", null));
        }

        Optional<ConfirmationCode> confirmationCodeOpt = confirmationCodeRepository.findByEmailAndCode(email, code);
        if (!confirmationCodeOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Noto'g'ri yoki mavjud bo'lmagan tasdiqlash kodi", null));
        }

        ConfirmationCode confirmationCode = confirmationCodeOpt.get();
        if (confirmationCode.getConfirmationDate().isBefore(Instant.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Kod muddati o'tgan", null));
        }

        session.setAttribute("codeVerified", true);
        confirmationCodeRepository.delete(confirmationCode);
        return ResponseEntity.ok(new SendMessage(true, "Kod muvaffaqiyatli kiritildi", null));
    }
   @Transactional
    @Override
    public ResponseEntity<?> resetPassword(HttpSession session, String newPassword, String confirmPassword) {
        String email = (String) session.getAttribute("email");
        Boolean isCodeVerified = (Boolean) session.getAttribute("codeVerified");

        if (email == null) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Email topilmadi", null));
        }
        if (isCodeVerified == null || !isCodeVerified) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Tasdiqlash kodi tekshirilmagan", null));
        }

        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new RuntimeException("Foydalanuvchi topilmadi"));

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Parollar bir-biriga mos kelmaydi", null));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        confirmationCodeRepository.deleteByEmail(email);
        session.removeAttribute("codeVerified");

        return ResponseEntity.ok(new SendMessage(true, "Parol muvaffaqiyatli yangilandi", null));
    }
}


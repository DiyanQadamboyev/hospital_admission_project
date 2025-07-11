
package org.example.hospital_admission_project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.config.JwtProvider;
import org.example.hospital_admission_project.entity.User;
import org.example.hospital_admission_project.payload.LoginDto;
import org.example.hospital_admission_project.payload.UserDto;
import org.example.hospital_admission_project.repo.UserRepository;
import org.example.hospital_admission_project.service.AuthService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.oauth2.login.OAuth2LoginSecurityMarker;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import static org.example.hospital_admission_project.utils.ApiConstants.API_PATH;

@RestController
@RequestMapping(ApiConstants.API_PATH + ApiConstants.API_VERSION)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final HttpSession session;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        return authService.signUp(userDto);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
            return authService.login(loginDto);
        }
    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestParam String email) {
        return authService.forgetPassword(session, email);
    }
    @PostMapping("/check-code")
    public ResponseEntity<?> checkCode(@RequestParam String code) {
        return authService.checkCode(session, code);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String newPassword, @RequestParam String confirmPassword) {
        return authService.resetPassword(session, newPassword, confirmPassword);
    }
    @GetMapping("/auth2/callback/google")
    @OAuth2LoginSecurityMarker
    public ResponseEntity<?> googleLogin(@AuthenticationPrincipal OAuth2User oauth2User) {
        String username = oauth2User.getAttribute("email"); // Or any other unique identifier
        User user = userRepository.findByEmail(username).orElse(null);
        if (user == null) {
            user = new User();
            user.setEmail(username);
            user.setPassword(passwordEncoder.encode("defaultPassword")); // Set a default password for the user
            user.setName(oauth2User.getAttribute("name"));
            userRepository.save(user);
        }
        String token = jwtProvider.generateToken(user.getEmail());
        return ResponseEntity.ok().body("JWT Token: " + token);
    }

    }

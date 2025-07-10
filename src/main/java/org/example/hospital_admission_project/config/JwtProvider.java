package org.example.hospital_admission_project.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Doctor;
import org.example.hospital_admission_project.entity.User;
import org.example.hospital_admission_project.entity.enums.Role;
import org.example.hospital_admission_project.repo.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import java.security.Key;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    public String generateToken(String  email){
        User user = userRepository.findByEmail(email);
        return "Bearer " + Jwts.builder()
                .subject(email)
                .claim("id", user.getId())
                .claim("role", user.getUserRole().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getKey())
                .compact();
    }
    public void setUserToContext(String email) {
        try {

            User user = (User) customUserDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e1) {
            try {

                Doctor doctor = (Doctor) customUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(doctor, null, doctor.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e2) {
                throw new RuntimeException("User or Doctor not found in authentication.");
            }
        }
    }
    public String getUsernameFromToken(String token){
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token).getBody();
    }
    public Key getKey(){
            return Keys.hmacShaKeyFor("secr213frvdqw1er23fg3tvewfq3grtvefcdet".getBytes());
        }
}

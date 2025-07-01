package org.example.hospital_admission_project.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Doctor;
import org.example.hospital_admission_project.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.security.Key;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final CustomUserDetailsService customUserDetailsService;

    public String generateToken(String  email){
        Long expireTimeout = (long) (20 * 24 * 60 * 60 * 1000);
        Date date =new Date(System.currentTimeMillis()+ expireTimeout);
        return Jwts
                .builder()
                .setIssuedAt(new Date())
                .setSubject(email)
                .setExpiration(date)
                .setIssuer("https://www.google.com")
                .signWith(getKey(), SignatureAlgorithm.HS256)
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

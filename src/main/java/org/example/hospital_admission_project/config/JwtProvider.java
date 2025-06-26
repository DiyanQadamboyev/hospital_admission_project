package org.example.hospital_admission_project.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.security.Key;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    public static final String key = "A665A45920422F9D417E4867EFDC4FB8A04A1F3FFF1FA07E998E86F7F7A27AE3";

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
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }
}

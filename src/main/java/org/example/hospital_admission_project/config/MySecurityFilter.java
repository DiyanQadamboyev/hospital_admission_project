package org.example.hospital_admission_project.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class MySecurityFilter extends OncePerRequestFilter {
   private final JwtProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest request = servletRequest;
        String authorization = request.getHeader("Authorization");
        if (authorization == null){
            filterChain.doFilter(request, servletResponse);
            return;
        }
        System.out.println(authorization);
        if (authorization.startsWith("Bearer ")){
            authorization = authorization.substring(7);
            String email = jwtProvider.getUsernameFromToken(authorization);
            jwtProvider.setUserToContext(email);
        }
        if (authorization.startsWith("Basic ")) {
            String auth = new String(Base64.getDecoder().decode(authorization)).substring(6);
            String[] split = auth.split(":");
            String email = split[0];
            jwtProvider.setUserToContext(email);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

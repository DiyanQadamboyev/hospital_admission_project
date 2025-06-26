package org.example.hospital_admission_project.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Doctor;
import org.example.hospital_admission_project.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtAuthenticatorFilter implements Filter {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authorization = request.getHeader("Authorization");
        if (authorization == null){
            filterChain.doFilter(request, servletResponse);
            return;
        }
        System.out.println(authorization);
        if (authorization.startsWith("Bearer ")){
            authorization = authorization.substring(7);
            String email = jwtProvider.getUsernameFromToken(authorization);
            setUserToContext(email);
        }
        if (authorization.startsWith("Basic ")) {
            String auth = new String(Base64.getDecoder().decode(authorization)).substring(6);
            String[] split = auth.split(":");
            String email = split[0];
            setUserToContext(email);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void setUserToContext(String email) {
        try {

            User user = (User) customUserDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e1) {
            try {

                Doctor doctor = (Doctor)customUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(doctor, null, doctor.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e2) {
                throw new RuntimeException("User or Doctor not found in authentication.");
            }
        }
    }
}

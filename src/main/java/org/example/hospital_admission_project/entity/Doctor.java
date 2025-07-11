package org.example.hospital_admission_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hospital_admission_project.entity.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Doctor implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private Double rating;
    @ManyToOne
    private Expert expert;
    @ElementCollection
    @CollectionTable(name = "doctor_times", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "available_time")
    private List<String> availableTimes = new ArrayList<>(
            Arrays.asList(
                    "08:00",
                    "09:00",
                    "10:00",
                    "11:00",
                    "13:00",
                    "14:00",
                    "15:00",
                    "16:00",
                    "17:00"
            ));
    private Double consultationPrice;
    @ManyToOne
    private Attachment attachment;
    private String location;
    private Role role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return "";
    }


}

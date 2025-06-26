package org.example.hospital_admission_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfirmationCode implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false , unique = true )
    private String code;
    @Email(message = "")
    @Column(nullable = false , unique = true )
    private String email;
    @Column(nullable = false , unique = true )
    private Instant confirmationDate;

}

package org.example.hospital_admission_project.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private Integer senderId;
    private Integer receiverId;
    private String message;
    private LocalDateTime dateTime;
}


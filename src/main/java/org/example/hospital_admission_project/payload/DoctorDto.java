package org.example.hospital_admission_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private Integer expertId;
    private Double consultationPrice;
    private String imageUrl;
    private String location;
}

package org.example.hospital_admission_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugsDetailsDTO {
    private Long drugId;
    private String imageUrl;
    private String drugName;
    private String drugUnit;
    private String description;
    private Integer quantity;
    private Double price;
    private Double rating;
}

package org.example.hospital_admission_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugDto {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Integer categoryId;
    private String drugUnit;
    private Integer attachmentId;
    private Double rating;
}

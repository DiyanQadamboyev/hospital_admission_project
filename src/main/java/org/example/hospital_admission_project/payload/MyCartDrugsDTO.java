package org.example.hospital_admission_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCartDrugsDTO {
    private Integer id;
    private String name;
    private Integer attachmentId;
    private String drugUnit;
    private Double price;
    private Double totalPrice;
    private Integer quantity;
}

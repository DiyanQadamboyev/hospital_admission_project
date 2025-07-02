package org.example.hospital_admission_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private  Long id;
    private Double subtotal;
    private Double taxes;
    private Double total;
    private Long<MyCartDrugsDTO> drugs = new ArrayList<>();
}

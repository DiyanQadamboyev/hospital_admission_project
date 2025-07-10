package org.example.hospital_admission_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Integer id;
    private Double subtotal;
    private Double taxes;
    private Double total;
    private List<MyCartDrugsDTO> drugs=new ArrayList<>();
}

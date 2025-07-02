package org.example.hospital_admission_project.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private String name;
    private String description;
    private String ImageUrl;
    private Integer categoryId;

}

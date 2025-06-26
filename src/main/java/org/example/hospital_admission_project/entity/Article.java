package org.example.hospital_admission_project.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date date;
    private String description;
    private String ImageUrl;
    @ManyToOne
    private ArticleCategory category;


}

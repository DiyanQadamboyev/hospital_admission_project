package org.example.hospital_admission_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hospital_admission_project.entity.enums.FileStatusStorage;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileStorage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String extension;
    private Long fileSize;
    private String contentType;
    private String hashId;
    private String uploadFolder;

    private FileStatusStorage fileStatusStorage;
}
package org.example.hospital_admission_project.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.hospital_admission_project.entity.Attachment;
import org.example.hospital_admission_project.repo.AttachmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public Integer upload(MultipartFile file) {
        String key = s3Service.uploadImage(file);
        String url = "https://" + bucketName + ".s3." + region + "." + "amazonaws.com/" + key;
        Attachment attachment = new Attachment();
        attachment.setImgUrl(url);
        Attachment save = attachmentRepository.save(attachment);
        return save.getId();
    }

    @SneakyThrows
    public void get(Integer attachmentId, HttpServletResponse response) {
        Attachment attachment = attachmentRepository.findById(attachmentId).orElse(null);
        byte[] image = s3Service.getImage(attachment.getImgUrl());
        response.getOutputStream().write(image);
    }
}

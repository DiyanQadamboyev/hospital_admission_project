package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;

    @SneakyThrows
    public String uploadImage(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String key = System.currentTimeMillis() + "_" + filename;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();
        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

        }
        return key;
    }

    @SneakyThrows
    public byte[] getImage(String imgUrl) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(imgUrl)
                .build();
        try (ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(getObjectRequest)) {
            return inputStream.readAllBytes();
        }
    }
}

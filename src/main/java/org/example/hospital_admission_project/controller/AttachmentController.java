package org.example.hospital_admission_project.controller;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.service.AttachmentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@MultipartConfig
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping
    public Integer uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return attachmentService.upload(file);

    }

    @GetMapping("/{attachmentId}")
    public void getFile(@PathVariable Integer attachmentId, HttpServletResponse response) throws IOException {
        attachmentService.get(attachmentId,response);

    }
}

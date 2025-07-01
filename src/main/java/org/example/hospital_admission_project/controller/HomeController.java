package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.service.HomeService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.example.hospital_admission_project.utils.ApiConstants.API_PATH;
import static org.example.hospital_admission_project.utils.ApiConstants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PATH + API_VERSION)
public class HomeController {
    private final HomeService service;

    @PostMapping("/search")
    public ResponseEntity<?> find(@RequestParam String text) {
        return service.search(text);
    }

    @GetMapping("/top-doctor")
    public ResponseEntity<?> findTopDoctors() {
        return service.topDoctors();
    }
    @GetMapping("/top-product")
    public ResponseEntity<?> findTopProducts() {
        return service.topProducts();
    }

}

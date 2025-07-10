package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.payload.DrugDto;
import org.example.hospital_admission_project.service.DrugsService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ApiConstants.API_PATH+ApiConstants.API_VERSION+"/product")
@RequiredArgsConstructor
public class DrugsController {
    private final DrugsService service;
    @GetMapping("/get/all")
    public ResponseEntity<?> getProducts() {
        return service.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@ModelAttribute DrugDto dto, @RequestParam("file") MultipartFile multipartFile) {

        return service.save(dto,multipartFile);
    }

    @PostMapping("/add/rating")
    public ResponseEntity<?> addRating(@RequestParam Long DrugId, @RequestParam Double rating) {
        return service.addRating(DrugId,rating);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return service.getProductId(id);
    }

    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<?> getProductName(@PathVariable String name) {
        return service.getProductName(name);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody DrugDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return service.delete(id);
    }

}

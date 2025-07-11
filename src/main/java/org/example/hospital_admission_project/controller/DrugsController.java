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
    public ResponseEntity<?> addProduct(@ModelAttribute DrugDto dto) {
        return service.save(dto);
    }

    @PostMapping("/add/rating")
    public ResponseEntity<?> addRating(@RequestParam Integer DrugId, @RequestParam Double rating) {
        return service.addRating(DrugId,rating);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Integer id) {
        return service.getProductId(id);
    }

    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<?> getProductName(@PathVariable String name) {
        return service.getProductName(name);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody DrugDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        return service.delete(id);
    }

}

package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.payload.ExpertDTO;
import org.example.hospital_admission_project.service.ExpertService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiConstants.API_PATH+ApiConstants.API_VERSION+"/expert")
@RestController
@RequiredArgsConstructor
public class ExpertController {
    private final ExpertService service;

    @GetMapping("/get/all")
    public ResponseEntity<?> getAll() {
        return service.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody ExpertDTO dto) {
        return service.save(dto);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Integer id) {
        return service.getExpertId(id);
    }

    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<?> getProductName(@PathVariable String name) {
        return service.getExpertName(name);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ExpertDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        return service.delete(id);
    }

}

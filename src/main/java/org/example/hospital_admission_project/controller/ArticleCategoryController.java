package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.payload.ArticleCategoryDto;
import org.example.hospital_admission_project.service.ArticleCategoryService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_PATH+ApiConstants.API_VERSION+"/article/category")
@RequiredArgsConstructor
public class ArticleCategoryController {
    private final ArticleCategoryService service;

    @GetMapping("/get/all")
    public ResponseEntity<?> getAll() {
        return service.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody ArticleCategoryDto dto) {
        return service.save(dto);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> getArticleCategory(@PathVariable Integer id) {
        return service.getArticleCategoryId(id);
    }

    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<?> getArticleCategoryName(@PathVariable String name) {
        return service.getArticleCategoryName(name);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateArticleCategory(@PathVariable Integer id, @RequestBody ArticleCategoryDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteArticleCategory(@PathVariable Integer id) {
        return service.delete(id);
    }
}

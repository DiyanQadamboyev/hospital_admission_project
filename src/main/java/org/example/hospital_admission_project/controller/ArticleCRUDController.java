package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.payload.ArticleDto;
import org.example.hospital_admission_project.service.ArticleCRUDService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_PATH+ApiConstants.API_VERSION)
@RequiredArgsConstructor
public class ArticleCRUDController {
    private final ArticleCRUDService service;

    @GetMapping("/get/all")
    public ResponseEntity<?> getAll() {
        return service.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ArticleDto dto) {
        return service.save(dto);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> getOneById(@PathVariable Integer id) {
        return service.getArticleId(id);
    }

    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<?> getName(@PathVariable String name) {
        return service.getArticleName(name);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ArticleDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return service.delete(id);
    }
}

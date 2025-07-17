package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.service.ArticleService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_PATH + ApiConstants.API_VERSION + "/user/articles")
@RequiredArgsConstructor
public class UArticlesController {

    private final ArticleService articleService;

    @PostMapping("/search/{SearchText}")
    public ResponseEntity<?> searchArticle(@PathVariable String SearchText) {
        return articleService.search(SearchText);
    }

    @GetMapping("/find/ArticleCategory/all")
    public ResponseEntity<?> getAllArticleCategory() {
        return articleService.getAllArticleCategory();
    }

    @GetMapping("/find/all")
    public ResponseEntity<?> getAllArticle() {
        return articleService.getAllArticle();
    }

    @PostMapping("/find/byArticleId/{ArticleId}")
    public ResponseEntity<?> getArticleById(@PathVariable Integer ArticleId) {
        return articleService.findId(ArticleId);
    }

    @PostMapping("/find/byCategoryId/{CategoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer CategoryId) {
        return articleService.findCategoryId(CategoryId);
    }

    @PostMapping("/find/byCategoryName/{CategoryName}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String CategoryName) {
        return articleService.findbyCategoryName(CategoryName);
    }

    @PostMapping("/find/byArticleName/{ArticleName}")
    public ResponseEntity<?> getArticleByName(@PathVariable String ArticleName) {
        return articleService.findName(ArticleName);
    }

}

package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Article;
import org.example.hospital_admission_project.entity.ArticleCategory;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.repo.ArticleCategoryRepository;
import org.example.hospital_admission_project.repo.ArticleRepository;
import org.example.hospital_admission_project.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


import static org.springframework.core.annotation.MergedAnnotations.search;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleCategoryService articleCategoryService;
    private final ArticleCRUDService articleCRUDService;

    public ResponseEntity<?> search(String searchText) {
        SendMessage sendMessage = search1(searchText);
        if (!sendMessage.success()) {
            SendMessage byCategoryName = findByCategoryName(searchText);
            if (!byCategoryName.success()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Article not found by Article name but Article found by Article Category name!", byCategoryName.data()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Article found by Article name!", sendMessage.data()));
    }

    private SendMessage search1(String searchText) {
        Optional<Article> optionalArticle = articleRepository.findByName(searchText);
        return optionalArticle.map(article -> new SendMessage(true, "", article)).orElseGet(() -> new SendMessage(false, "", null));
    }

    private SendMessage findByCategoryName(String categoryName) {
        Optional<ArticleCategory> optionalArticleCategory = articleCategoryService.getName(categoryName);
        if (optionalArticleCategory.isEmpty()) {
            return new SendMessage(false, "", null);
        }
        ArticleCategory category = optionalArticleCategory.get();
        List<Article> articleList = articleRepository.findArticlesByCategory(category);
        if (articleList.isEmpty()) {
            return new SendMessage(false, "", null);
        }
        return new SendMessage(true, "Articles found!", articleList);

    }

    public ResponseEntity<?> getAllArticleCategory() {
        return articleCategoryService.getAll();
    }

    public ResponseEntity<?> getAllArticle() {
        return articleCRUDService.getAll();
    }

    public ResponseEntity<?> findId(Integer articleId) {
        return articleCRUDService.getArticleId(articleId);
    }

    public ResponseEntity<?> findCategoryId(Integer categoryId) {
        ArticleCategory category = articleCategoryService.getId(categoryId);
        List<Article> articlesByCategory = articleRepository.findArticlesByCategory(category);
        if (articlesByCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Article not found by Category id!", categoryId));
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(new SendMessage(true, "Article found!", articlesByCategory));
    }

    public ResponseEntity<?> findbyCategoryName(String categoryName) {
        Optional<ArticleCategory> optionalArticleCategory = articleCategoryService.getName(categoryName);
        if (optionalArticleCategory.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "ArticleCategory not found by ArticleCategory name!", categoryName));
        }
        ArticleCategory category = optionalArticleCategory.get();
        List<Article> articleList = articleRepository.findArticlesByCategory(category);
        if (articleList.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Article not found by ArticleCategory name!", categoryName));
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(new SendMessage(true, "Articles found!", articleList));

    }

    public ResponseEntity<?> findName(String articleName) {
        return articleCRUDService.getArticleName(articleName);
    }
}

package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.ArticleCategory;
import org.example.hospital_admission_project.entity.enums.Role;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.payload.ArticleCategoryDto;
import org.example.hospital_admission_project.repo.ArticleCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleCategoryService {
    private final ArticleCategoryRepository articleCategoryRepository;
    private final UserService userService;

    public ArticleCategory getId(Integer categoryId) {
        if (articleCategoryRepository.findById(categoryId).isEmpty()) {
            return null;
        }
        return articleCategoryRepository.findById(categoryId).get();
    }

    public Optional<ArticleCategory> getName(String categoryName) {
        return articleCategoryRepository.findByName(categoryName);
    }

    public ResponseEntity<?> getAll() {
        List<ArticleCategory> all = articleCategoryRepository.findAll();
        if (all.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SendMessage(false, "ArticleCategory list empty!", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "All ArticleCategory", all));
    }

    public ResponseEntity<?> save(ArticleCategoryDto dto) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", null));
        }
        ArticleCategory category = new ArticleCategory();
        if (dto.getName() == null | dto.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Name is required!", dto));
        }
        if (articleCategoryRepository.findByName(dto.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SendMessage(false, "ArticleCategory already exist!", dto));
        }
        category.setName(dto.getName());
        articleCategoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessage(true, "ArticleCategory successfully saved!", category));
    }

    public ResponseEntity<?> getArticleCategoryId(Integer id) {
        Optional<ArticleCategory> optionalArticleCategory = articleCategoryRepository.findById(id);
        if (optionalArticleCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "ArticleCategory not found!", id));
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalArticleCategory.get());
    }

    public ResponseEntity<?> getArticleCategoryName(String name) {
        Optional<ArticleCategory> byName = articleCategoryRepository.findByName(name);
        if (byName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "ArticleCategory not found!", name));
        }
        return ResponseEntity.status(HttpStatus.OK).body(byName);
    }

    public ResponseEntity<?> update(Integer id, ArticleCategoryDto dto) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", null));
        }
        Optional<ArticleCategory> optionalArticleCategory = articleCategoryRepository.findById(id);
        if (optionalArticleCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "ArticleCategory not found!", id));
        }
        if (dto.getName() == null | dto.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Name is required!", dto));
        }
        ArticleCategory category = optionalArticleCategory.get();
        category.setName(dto.getName());
        articleCategoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "ArticleCategory successfully updated!", category));

    }

    public ResponseEntity<?> delete(Integer id) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", null));
        }
        articleCategoryRepository.findById(id).ifPresent(articleCategoryRepository::delete);
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "ArticleCategory successfully deleted!", id));

    }
}

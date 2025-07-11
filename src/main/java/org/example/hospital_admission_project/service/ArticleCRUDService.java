package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Article;
import org.example.hospital_admission_project.entity.ArticleCategory;
import org.example.hospital_admission_project.entity.Attachment;
import org.example.hospital_admission_project.entity.enums.Role;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.payload.ArticleDto;
import org.example.hospital_admission_project.repo.ArticleRepository;
import org.example.hospital_admission_project.repo.AttachmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleCRUDService {
    private final UserService userService;
    private final ArticleRepository articleRepository;
    private final ArticleCategoryService articleCategoryService;
    private final AttachmentRepository attachmentRepository;

    public ResponseEntity<?> getAll() {
        List<Article> all = articleRepository.findAll();
        if (all.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SendMessage(false, "Article list empty!", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "all Articles!", all));
    }

    public ResponseEntity<?> getArticleName(String name) {
        Optional<Article> optionalArticles = articleRepository.findByName(name);
        return optionalArticles.map(article -> ResponseEntity.status(HttpStatus.OK).body(
                new SendMessage(true, "Article found!", article))).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Article not found!", name)));
    }

    public ResponseEntity<?> save(ArticleDto dto) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", null));
        }
        ArticleCategory articleCategory = articleCategoryService.getId(dto.getCategoryId());
        if (articleCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Article Category not found!", dto.getCategoryId()));
        }
        if (dto.getName() == null || dto.getName().isBlank() || dto.getDescription() == null || dto.getDescription().isBlank() || dto.getAttachmentId().toString().isBlank()) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Name or Description or Image url cannot be empty!", dto));
        }
        Optional<Article> name = articleRepository.findByName(dto.getName());
        if (name.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SendMessage(false, "Article already exist!", dto.getName()));
        }
        Article article = new Article();
        article.setName(dto.getName());
        article.setDate(new Date(System.currentTimeMillis()));
        article.setCategory(articleCategory);
        article.setDescription(dto.getDescription());
        Attachment attachment = attachmentRepository.findById(dto.getAttachmentId()).orElse(null);
        article.setAttachment(attachment);
        articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessage(true, "Article saved!", article));
    }

    public ResponseEntity<?> getArticleId(Integer id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        return optionalArticle.map(article -> ResponseEntity.status(HttpStatus.FOUND)
                        .body(new SendMessage(true, "Article found!", article)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new SendMessage(false, "Article not found!", id)));
    }

    public ResponseEntity<?> update(Integer id, ArticleDto dto) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", role));
        }
        Optional<Article> optionalArticle = articleRepository.findById(id);
        ArticleCategory articleCategory = articleCategoryService.getId(dto.getCategoryId());

        if (optionalArticle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Article not found!", id));
        }
        if (articleCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Article Category not found!", dto.getCategoryId()));
        }
        if (dto.getName() == null | dto.getName().isBlank() | dto.getDescription() == null | dto.getDescription().isBlank() | dto.getAttachmentId().toString().isBlank() | dto.getAttachmentId() == null) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Name or Description or Image url cannot be empty!", dto));
        }
        Optional<Article> name = articleRepository.findByName(dto.getName());
        if (name.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SendMessage(false, "Article already exist!", dto.getName()));
        }
        Article article = optionalArticle.get();
        article.setName(dto.getName());
        article.setCategory(articleCategory);
        article.setDescription(dto.getDescription());
        Attachment attachment = attachmentRepository.findById(dto.getAttachmentId()).orElse(null);
        article.setAttachment(attachment);
        articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SendMessage(true, "Article successfully updated!", article));
    }

    public ResponseEntity<?> delete(Integer id) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", role));
        }
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Article not found!", id));
        }
        articleRepository.delete(optionalArticle.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SendMessage(true, "Article deleted!", id));
    }
}

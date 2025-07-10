package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Article;
import org.example.hospital_admission_project.entity.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findByName(String name);

    List<Article> findArticlesByCategory(ArticleCategory category);
}
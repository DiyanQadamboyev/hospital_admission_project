package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
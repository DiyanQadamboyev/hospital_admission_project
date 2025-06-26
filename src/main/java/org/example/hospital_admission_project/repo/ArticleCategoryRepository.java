package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Integer> {
}
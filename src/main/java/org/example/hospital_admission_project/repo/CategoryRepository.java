package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
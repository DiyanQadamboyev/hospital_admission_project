package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Category;
import org.example.hospital_admission_project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrugsRepository extends JpaRepository<Product, Long> {

    List<Product> findProductsByCategory(Category category);
}

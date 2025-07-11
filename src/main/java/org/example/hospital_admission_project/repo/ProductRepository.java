package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Category;
import org.example.hospital_admission_project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByName(String name);

    List<Product> findAllByName(String text);

    List<Product> findProductsByCategory(Category category);

    List<Product> findByRatingBetween(double v, double v1);
}
package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {
}
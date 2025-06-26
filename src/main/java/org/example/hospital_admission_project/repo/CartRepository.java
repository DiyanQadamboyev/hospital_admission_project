package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
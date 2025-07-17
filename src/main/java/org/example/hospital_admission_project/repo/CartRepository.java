package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findCartByUserId(Integer id);
}
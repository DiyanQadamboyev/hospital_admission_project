package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Category;
import org.example.hospital_admission_project.entity.Product;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyService {
    private final CategoryService categoryService;
    private final DrugsService drugsService;

    public ResponseEntity<?> search(String text) {
        List<Product> productListBYProductName = drugsService.getProductListBYProductName(text);
        if (productListBYProductName.isEmpty()) {
            Category category = categoryService.getCategoryBYCategoryName(text);
            if (category == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Drugs and Category not found", null));
            }
            List<Product> productListBYCategoryName = drugsService.getProductListBYCategoryName(category);
            if (productListBYCategoryName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "No products found", null));
            }

            return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Drugs not found by Drug name but Drugs found by Category name!", productListBYCategoryName));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Drugs found by Drug name", productListBYProductName));

    }
}

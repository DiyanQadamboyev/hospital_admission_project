package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Category;
import org.example.hospital_admission_project.entity.Doctor;
import org.example.hospital_admission_project.entity.Expert;
import org.example.hospital_admission_project.entity.Product;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.repo.DoctorRepository;
import org.example.hospital_admission_project.repo.DrugsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final DoctorRepository doctorRepository;
    private final DoctorService doctorService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ExpertService expertService;
    private final DrugsRepository drugsRepository;

    public ResponseEntity<?> search(String text) {
        Doctor doctor = doctorService.findDoctorName(text);
        Product product = productService.findProductName(text);
        Category category = categoryService.findCategoryName(text);
        Expert expert = expertService.findExpertName(text);

        if (doctor != null) {
            return ResponseEntity.status(HttpStatus.FOUND).body(new SendMessage(true, "Doctor found!", doctor));
        }
        if (product != null) {
            return ResponseEntity.status(HttpStatus.FOUND).body(new SendMessage(true, "Product found!", product));
        }
        if (category != null) {
            List<Product> productsByCategory = drugsRepository.findProductsByCategory(category);
            return ResponseEntity.status(HttpStatus.FOUND).body(new SendMessage(true, "Category found!", productsByCategory));
        }
        if (expert != null) {
            List<Doctor> byExpertId = doctorRepository.findByExpert_Id(expert.getId());
            return ResponseEntity.status(HttpStatus.FOUND).body(new SendMessage(true, "Expert found!", byExpertId));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SendMessage(false, "No content!", null));
    }

    public ResponseEntity<?> topDoctors() {
        List<Doctor> topDoctors = doctorService.findByRating();
        return ResponseEntity.status(HttpStatus.FOUND).body(new SendMessage(true, "Top Doctors found!", topDoctors));
    }

    public ResponseEntity<?> topProducts() {
        List<Product> topProducts = productService.findByRating();
        return ResponseEntity.status(HttpStatus.FOUND).body(new SendMessage(true, "Top Products found!", topProducts));
    }
}

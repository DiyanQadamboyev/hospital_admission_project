package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.Category;
import org.example.hospital_admission_project.entity.FileStorage;
import org.example.hospital_admission_project.entity.Product;
import org.example.hospital_admission_project.entity.Rating;
import org.example.hospital_admission_project.entity.enums.Role;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.payload.DrugDto;
import org.example.hospital_admission_project.repo.DrugsRepository;
import org.example.hospital_admission_project.repo.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrugsService {
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    public List<Product> getProductListBYProductName(String text) {

    }

    public ResponseEntity<?> save(DrugDto dto, MultipartFile multipartFile) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", null));
        }
        Category category = categoryService.getId(dto.getCategoryId());
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Category not found!", dto.getCategoryId()));
        }
        if (dto.getName() == null | dto.getName().isBlank() | dto.getDescription() == null | dto.getDescription().isBlank() | dto.getDrugUnit().isBlank() | dto.getDrugUnit() == null) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Name or Description or Image url or Drug Unit cannot be empty!", dto));
        }
        Optional<Product> name = productRepository.findByName(dto.getName());
        if (name.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SendMessage(false, "Product already exist!", dto.getName()));
        }
        FileStorage fileStorage = fileStorageService.save(multipartFile);
        Product product = new Product();
        productRepository.save(product);
        product.setName(dto.getName());
        product.setCategory(category);
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDrugUnit(dto.getDrugUnit());
        product.setQuantity(dto.getQuantity());
        product.setFileStorage(fileStorage);
        if (dto.getRating() < 0 | dto.getRating() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Rating is out of range!", dto));
        }

        Rating rating = new Rating();
        ratingRepository.save(rating);
        rating.getRatings().add(dto.getRating());
        rating.setOwnerId(product.getId());
        rating.setOwnerRating(ratingService.ratingService(rating));
        ratingRepository.save(rating);
        product.setRating(rating.getOwnerRating());

//        product.setFileStorage(fileStorageService.save(dto.getFileStorage()));
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessage(true, "Product saved!", product));
    }

    public ResponseEntity<?> getAll() {
        List<Product> all = productRepository.findAll();
        if (all.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SendMessage(false, "product list empty!", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "All products!", all));
    }
}

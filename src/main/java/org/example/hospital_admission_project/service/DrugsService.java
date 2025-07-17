package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.*;
import org.example.hospital_admission_project.entity.enums.Role;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.payload.DrugDto;
import org.example.hospital_admission_project.repo.AttachmentRepository;
import org.example.hospital_admission_project.repo.DrugsRepository;
import org.example.hospital_admission_project.repo.ProductRepository;
import org.example.hospital_admission_project.repo.RatingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final RatingRepository ratingRepository;
    private final RatingService ratingService;
    private final AttachmentRepository attachmentRepository;

    public List<Product> getProductListBYProductName(String text) {
        return productRepository.findAllByName(text);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(DrugDto dto) {
        Category category = categoryService.getId(dto.getCategoryId());
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Category not found!", dto.getCategoryId()));
        }
        if (dto.getName() == null || dto.getName().isBlank() || dto.getDescription() == null || dto.getDescription().isBlank() || dto.getDrugUnit().isBlank() || dto.getAttachmentId().toString().isEmpty()) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Name or Description or Image url or Drug Unit cannot be empty!", dto));
        }
        Optional<Product> name = productRepository.findByName(dto.getName());
        if (name.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SendMessage(false, "Product already exist!", dto.getName()));
        }

        Product product = new Product();
        productRepository.save(product);
        product.setName(dto.getName());
        product.setCategory(category);
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDrugUnit(dto.getDrugUnit());
        product.setQuantity(dto.getQuantity());
        Attachment attachment = attachmentRepository.findById(dto.getAttachmentId()).orElse(null);
        product.setAttachment(attachment);
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

    public ResponseEntity<?> addRating(Integer drugId, Double rating) {
        Optional<Rating> optionalRating = ratingRepository.findByOwnerId(drugId);
        Optional<Product> optionalProduct = productRepository.findById(drugId);
        User user = userService.getUser();

        if (optionalRating.isEmpty() || optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Rating or Drug not found!", rating));
        }
        Rating rating1 = optionalRating.get();
        for (Integer l : rating1.getUsersId()) {
            if (l.equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new SendMessage(false, "Rating already exists!", rating1));
            }

        }
        Product product = optionalProduct.get();
        if (rating > 0 | rating < 6) {
            rating1.getRatings().add(rating);
            rating1.getUsersId().add(user.getId());
            rating1.setOwnerRating(ratingService.ratingService(rating1));
            ratingRepository.save(rating1);
            product.setRating(rating1.getOwnerRating());
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SendMessage(true, "Rating added!", rating1));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Rating not added!", rating));

    }

    public ResponseEntity<?> getProductId(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(product -> ResponseEntity.status(HttpStatus.FOUND)
                        .body(new SendMessage(true, "Product found!", product)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new SendMessage(false, "Product not found!", id)));
    }

    public ResponseEntity<?> getProductName(String name) {
        Optional<Product> optionalProducts = productRepository.findByName(name);
        return optionalProducts.map(product -> ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Product found!", product))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Product not found!", name)));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(Integer id, DrugDto dto) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        Category category = categoryService.getId(dto.getCategoryId());

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Product not found!", id));
        }
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Category not found!", dto.getCategoryId()));
        }
        if (dto.getName() == null || dto.getName().isBlank() || dto.getDescription() == null || dto.getDescription().isBlank() || dto.getDrugUnit().isBlank() || dto.getAttachmentId().toString().isEmpty()) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Name or Description or Image url or Drug Unit cannot be empty!", dto));
        }
        Optional<Product> name = productRepository.findByName(dto.getName());
        if (name.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SendMessage(false, "Product already exist!", dto.getName()));
        }
        Product product = optionalProduct.get();
        product.setName(dto.getName());
        product.setCategory(category);
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        Attachment attachment = attachmentRepository.findById(dto.getAttachmentId()).orElse(null);
        product.setAttachment(attachment);
//        product.setFileStorage(fileStorageService.save(dto.getFileStorage()));
        product.setDrugUnit(dto.getDrugUnit());
        Optional<Rating> optionalRating = ratingRepository.findByOwnerId(product.getId());
        if (optionalRating.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Rating not found!", dto));
        }
        Rating rating1 = optionalRating.get();
        if (dto.getRating() < 0 | dto.getRating() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Bad Rating", dto.getRating()));
        }
        rating1.getRatings().add(dto.getRating());
        rating1.setOwnerRating(ratingService.ratingService(rating1));
        ratingRepository.save(rating1);
        product.setRating(rating1.getOwnerRating());
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SendMessage(true, "Product successfully updated!", product));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(Integer id) {
        productRepository.findById(id).ifPresent(productRepository::delete);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SendMessage(true, "Product deleted!", id));
    }

    public List<Product> getProductListBYCategoryName(Category category) {
        return productRepository.findProductsByCategory(category);
    }

    public Product findProductName(String text) {
        Optional<Product> optionalProduct = productRepository.findByName(text);
        return optionalProduct.orElse(null);
    }

    public List<Product> findByRating() {
        return productRepository.findByRatingBetween(3.9, 5.0);
    }
}

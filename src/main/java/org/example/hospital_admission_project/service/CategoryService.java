package org.example.hospital_admission_project.service;

import org.example.hospital_admission_project.entity.Category;
import org.example.hospital_admission_project.entity.enums.Role;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.payload.CategoryDTO;
import org.example.hospital_admission_project.repo.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public CategoryService(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public ResponseEntity<?> getAll() {
        List<Category> all = categoryRepository.findAll();
        if (all.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SendMessage(false, "Category list empty!", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "All Category", all));
    }

    public ResponseEntity<?> save(CategoryDTO dto) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", null));
        }
        Category category = new Category();
        if (dto.getName() == null | dto.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Name is required!", dto));
        }
        if (categoryRepository.findByName(dto.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new SendMessage(false, "Category already exist!", dto));
        }
        category.setName(dto.getName());
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessage(true, "Category successfully saved!", category));
    }

    public ResponseEntity<?> getCategoryId(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Category not found!", id));
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalCategory.get());
    }

    public ResponseEntity<?> getCategoryName(String name) {
        Optional<Category> byName = categoryRepository.findByName(name);
        if (byName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Category not found!", name));
        }
        return ResponseEntity.status(HttpStatus.OK).body(byName);
    }

    public ResponseEntity<?> update(Integer id, CategoryDTO dto) {
        Role role = userService.getRole();
        if (!role.equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().body(new SendMessage(false, "Faqat Admin huquqi bor! ", null));
        }
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Category not found!", id));
        }
        if (dto.getName() == null | dto.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Name is required!", dto));
        }
        Category category = optionalCategory.get();
        category.setName(dto.getName());
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Category successfully updated!", category));

    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(Integer id) {
        categoryRepository.findById(id).ifPresent(categoryRepository::delete);
        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "Category successfully deleted!", id));
    }

    public Category getId(Integer id) {
        if (categoryRepository.findById(id).isEmpty()) {
            return null;
        }
        return categoryRepository.findById(id).get();
    }


    public Category findCategoryName(String name) {
        Optional<Category> byName = categoryRepository.findByName(name);
        return byName.orElse(null);
    }

    public Category getCategoryBYCategoryName(String text) {
        Optional<Category> byName = categoryRepository.findByName(text);
        if (byName.isEmpty()) {
            return null;
        }
        return byName.get();

    }
}
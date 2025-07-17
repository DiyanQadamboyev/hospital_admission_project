package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.*;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.payload.DrugsDetailsDTO;
import org.example.hospital_admission_project.repo.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrugsDetailsService {
    private final DrugsRepository drugsRepository;
    private final AttachmentRepository attachmentRepository;
    private final RatingRepository ratingRepository;
    private final RatingService ratingService;
    private final CartProductRepository cartProductRepository;
    private final UserService userService;
    private final CartService cartService;
    private final CartRepository cartRepository;

    public ResponseEntity<?> findDoriById(Integer doriId) {
        Optional<Product> byId = drugsRepository.findById(doriId);
        if (byId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "Dori not found!", null));
        }
        Product product = byId.get();
        DrugsDetailsDTO dto=new DrugsDetailsDTO();
        dto.setDrugId(product.getId());
        attachmentRepository.findById(product.getAttachment().getId())
                .ifPresent(attachment -> dto.setAttachmentId(attachment.getId()));
        dto.setDrugName(product.getName());
        dto.setDrugUnit(product.getDrugUnit());
        dto.setRating(product.getRating());
        dto.setQuantity(product.getQuantity());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());

        return ResponseEntity.status(HttpStatus.OK).body(new SendMessage(true, "find Dori", dto));
    }


    public ResponseEntity<?> addRating(Integer doriId, Double rating) {
        Optional<Product> optionalProduct = drugsRepository.findById(doriId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "drugs not found!", doriId));
        }
        if (rating < 0 | rating > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SendMessage(false, "Invalid rating!", rating));
        }
        Product product = optionalProduct.get();
        Optional<Rating> optionalRating = ratingRepository.findByOwnerId(product.getId());
        optionalRating.get().getRatings().add(rating);
        optionalRating.get().setOwnerRating(ratingService.ratingService(optionalRating.get()));
        ratingRepository.save(optionalRating.get());
        product.setRating(optionalRating.get().getOwnerRating());
        drugsRepository.save(product);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SendMessage(true, "Rating updated", null));
    }

    public ResponseEntity<?> DrugAddCart(Integer doriId) {
        Optional<Product> optionalProduct = drugsRepository.findById(doriId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "drugs not found!", null));
        }
        Product product = optionalProduct.get();
        if (product.getQuantity() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SendMessage(false, "drugs not found!", null));
        }
        User user = userService.getUser();
        Optional<Cart> optionalCart = cartRepository.findCartByUserId(user.getId());

        if (optionalCart.isEmpty()) {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setProductId(product.getId());
            product.setQuantity(product.getQuantity() - 1);
            drugsRepository.save(product);
            cartProduct.setQuantity(1);
            cartProductRepository.save(cartProduct);
            Cart cart = new Cart();
            cart.setUserId(user.getId());
            cart.getProducts().add(cartProduct);
            cartService.addCart(cart);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessage(true, "Cart created and drugs added in Cart", cart));
        } else {
            Cart cart = optionalCart.get();
            for (CartProduct cartProduct : cart.getProducts()) {
                if (cartProduct.getProductId().equals(product.getId())) {
                    cartProduct.setQuantity(cartProduct.getQuantity() + 1);
                    product.setQuantity(product.getQuantity() - 1);
                    drugsRepository.save(product);
                    cartProductRepository.save(cartProduct);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SendMessage(true, "Cart quantity updated and drugs added", cartProduct.getQuantity()));
                }
            }
            CartProduct cartProduct = new CartProduct();
            cartProduct.setProductId(product.getId());
            product.setQuantity(product.getQuantity() - 1);
            drugsRepository.save(product);
            cartProduct.setQuantity(1);
            cartProductRepository.save(cartProduct);
            cart.getProducts().add(cartProduct);
            cartService.addCart(cart);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SendMessage(true, "Cart updated", cartProduct));
        }
    }
}

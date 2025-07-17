package org.example.hospital_admission_project.service;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.entity.*;
import org.example.hospital_admission_project.entity.sendMessage.SendMessage;
import org.example.hospital_admission_project.payload.CartDTO;
import org.example.hospital_admission_project.payload.MyCartDrugsDTO;
import org.example.hospital_admission_project.repo.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final DrugsRepository drugsRepository;
    private final AttachmentRepository attachmentRepository;
    private final CartProductRepository cartProductRepository;

    public ResponseEntity<?> getCart() {
        User user = userService.getUser();
        Optional<Cart> optionalCart = cartRepository.findCartByUserId(user.getId());
        if (optionalCart.isPresent()) {
            CartDTO cartdto = new CartDTO();
            cartdto.setId(optionalCart.get().getId());
            cartdto.setTaxes(100.0);
            cartdto.setSubtotal(0.0);
            cartdto.setTotal(0.0);

            for (CartProduct cp : optionalCart.get().getProducts()) {
                drugsRepository.findById(cp.getProductId()).ifPresent(c -> {
                    MyCartDrugsDTO drugdto = new MyCartDrugsDTO();
                    drugdto.setId(c.getId());
                    drugdto.setName(c.getName());
                    drugdto.setPrice(c.getPrice());
                    drugdto.setQuantity(cp.getQuantity());

                    attachmentRepository.findById(c.getAttachment().getId())
                            .ifPresent(attachment -> drugdto.setAttachmentId(attachment.getId()));

                    drugdto.setDrugUnit(c.getDrugUnit());
                    drugdto.setTotalPrice(c.getPrice() * cp.getQuantity());

                    cartdto.getDrugs().add(drugdto);
                    cartdto.setSubtotal(cartdto.getSubtotal() + drugdto.getTotalPrice());
                });
            }

            cartdto.setTotal(cartdto.getSubtotal() + cartdto.getTaxes());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SendMessage(true, "malumotlar o'tqazildi!", cartdto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new SendMessage(false, "Cart not found!", null));
    }

    public ResponseEntity<?> deleteDoriByCart(Integer doriId) {
        User user = userService.getUser();
        Optional<Cart> optionalCart = cartRepository.findCartByUserId(user.getId());
        if (optionalCart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SendMessage(false, "Cart not found!", null));
        }
        Cart cart = optionalCart.get();
        CartProduct found = null;
        for (CartProduct cartProduct : cart.getProducts()) {
            if (cartProduct.getProductId().equals(doriId)) {
                found = cartProduct;
                break;
            }
        }
        if (found == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SendMessage(false, "Dori savatda topilmadi!", doriId));
        }
        Optional<Product> optionalProduct = drugsRepository.findById(doriId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SendMessage(false, "Dori bazadan topilmadi!", doriId));
        }

        Product product = optionalProduct.get();
        product.setQuantity(product.getQuantity() + 1);
        drugsRepository.save(product);

        if (found.getQuantity() > 1) {
            found.setQuantity(found.getQuantity() - 1);
            cartProductRepository.save(found);
        } else {
            cart.getProducts().remove(found);
            cartProductRepository.delete(found);
        }

        cartRepository.save(cart);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new SendMessage(true, "Dori savatdan o‘chirildi yoki miqdori kamaytirildi", null));
    }

    public ResponseEntity<?> checkout(Integer summa, String cardNumber) {
        if (summa == null || summa <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SendMessage(false, "Summa noto‘g‘ri!", summa));
        }

        if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.matches("\\d{16}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SendMessage(false, "Karta raqami 16 xonali bo‘lishi kerak!", cardNumber));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SendMessage(true, "To‘lov muvaffaqiyatli amalga oshirildi!", summa));
    }

    public void addCart(Cart cart) {
        cartRepository.save(cart);
    }
}

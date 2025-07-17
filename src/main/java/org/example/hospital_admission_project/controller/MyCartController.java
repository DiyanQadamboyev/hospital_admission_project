package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.service.CartService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_PATH+ApiConstants.API_VERSION+"/myCart")
@RequiredArgsConstructor
public class MyCartController {

    private final CartService cartService;

    @GetMapping("/get")
    public ResponseEntity<?> getCart() {
        return cartService.getCart();
    }

    @PostMapping("/delete-dori")
    public ResponseEntity<?> deleteCart(@RequestParam Integer DoriId) {
        return cartService.deleteDoriByCart(DoriId);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestParam Integer summa, @RequestParam String CardNumber) {
        return cartService.checkout(summa, CardNumber);
    }
}

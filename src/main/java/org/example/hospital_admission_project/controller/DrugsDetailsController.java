package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospital_admission_project.service.DrugsDetailsService;
import org.example.hospital_admission_project.service.PharmacyService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_PATH+ApiConstants.API_VERSION+"/user/drugsDetails")
@RequiredArgsConstructor
public class DrugsDetailsController {
private final DrugsDetailsService drugsDetailsService;


    @PostMapping("/findDoriById")
    public ResponseEntity<?> findDrugsByDoriId(@RequestParam Integer DoriId) {
        return drugsDetailsService.findDoriById(DoriId);
    }

    @PostMapping("/addRatingToDori")
    public ResponseEntity<?> addRating(@RequestParam Integer DoriId, @RequestParam Double Rating) {
        return drugsDetailsService.addRating(DoriId, Rating);
    }

    @PostMapping("/addDoriToCart")
    public ResponseEntity<?> addDoriToCart(@RequestParam Integer DoriId) {
        return drugsDetailsService.DrugAddCart(DoriId);
    }

}

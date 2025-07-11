package org.example.hospital_admission_project.controller;

import lombok.RequiredArgsConstructor;

import org.example.hospital_admission_project.service.PharmacyService;
import org.example.hospital_admission_project.utils.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_PATH+ApiConstants.API_VERSION+"/user/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {
    private final PharmacyService pharmacyService;

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestParam String text) {
        return pharmacyService.search(text);
    }

    @PostMapping("/find/byDoriId")
    public ResponseEntity<?> findByDoriID(@RequestParam Integer DoriId) {
        return pharmacyService.findDrugbydrugId(DoriId);
    }

    @GetMapping("/find/allDrugs")
    public ResponseEntity<?> findAllDrugs() {
        return pharmacyService.getAllDrugs();
    }

}

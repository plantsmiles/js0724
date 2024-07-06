package com.js0724.tool_rental.controller;

import com.js0724.tool_rental.dto.CheckoutRequest;
import com.js0724.tool_rental.model.RentalAgreement;
import com.js0724.tool_rental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequest request) {
        RentalAgreement agreement = rentalService.checkout(request);
        String printedAgreement = rentalService.printRentalAgreement(agreement);
        return ResponseEntity.ok(printedAgreement);
    }

    @GetMapping("/agreements")
    public List<RentalAgreement> getAllRentalAgreements() {
        return rentalService.getAllRentalAgreements();
    }

    @GetMapping("/agreements/{id}")
    public ResponseEntity<RentalAgreement> getRentalAgreementById(@PathVariable Long id) {
        return rentalService.getRentalAgreementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estimate")
    public ResponseEntity<String> estimateRental(@RequestBody CheckoutRequest request) {
        RentalAgreement estimatedAgreement = rentalService.estimateRental(request);
        String estimateDetails = rentalService.printRentalAgreement(estimatedAgreement);
        return ResponseEntity.ok(estimateDetails);
    }
}
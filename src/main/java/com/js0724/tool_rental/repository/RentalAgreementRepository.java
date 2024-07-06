package com.js0724.tool_rental.repository;

import com.js0724.tool_rental.model.RentalAgreement;
import com.js0724.tool_rental.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, Long> {
    
    // Find rental agreements by tool
    List<RentalAgreement> findByTool(Tool tool);
    
    // Find rental agreements by checkout date
    List<RentalAgreement> findByCheckoutDate(LocalDate checkoutDate);
    
    // Find rental agreements with checkout dates within a range
    List<RentalAgreement> findByCheckoutDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find rental agreements by due date
    List<RentalAgreement> findByDueDate(LocalDate dueDate);
    
    // Find rental agreements with a discount
    List<RentalAgreement> findByDiscountPercentGreaterThan(int discountPercent);
}
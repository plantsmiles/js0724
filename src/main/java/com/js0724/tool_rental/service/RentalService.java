package com.js0724.tool_rental.service;

import com.js0724.tool_rental.dto.CheckoutRequest;
import com.js0724.tool_rental.exception.InvalidDiscountPercentException;
import com.js0724.tool_rental.exception.InvalidRentalDayCountException;
import com.js0724.tool_rental.model.*;
import com.js0724.tool_rental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RentalService {

    private final ToolRepository toolRepository;
    private final HolidayRepository holidayRepository;
    private final RentalAgreementRepository rentalAgreementRepository;

    @Autowired
    public RentalService(ToolRepository toolRepository, HolidayRepository holidayRepository, 
                         RentalAgreementRepository rentalAgreementRepository) {
        this.toolRepository = toolRepository;
        this.holidayRepository = holidayRepository;
        this.rentalAgreementRepository = rentalAgreementRepository;
    }

    @Transactional
    public RentalAgreement checkout(CheckoutRequest request) {
        validateCheckoutRequest(request);

        Tool tool = toolRepository.findById(request.getToolCode())
                .orElseThrow(() -> new IllegalArgumentException("Tool not found"));

        LocalDate dueDate = calculateDueDate(request.getCheckoutDate(), request.getRentalDayCount());
        int chargeDays = calculateChargeDays(tool, request.getCheckoutDate(), dueDate);
        
        BigDecimal dailyCharge = BigDecimal.valueOf(tool.getType().getDailyCharge());
        BigDecimal preDiscountCharge = calculatePreDiscountCharge(dailyCharge, chargeDays);
        BigDecimal discountAmount = calculateDiscountAmount(preDiscountCharge, request.getDiscountPercent());
        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        RentalAgreement agreement = new RentalAgreement(
            tool,
            request.getRentalDayCount(),
            request.getCheckoutDate(),
            dueDate,
            dailyCharge.doubleValue(),
            chargeDays,
            preDiscountCharge.doubleValue(),
            request.getDiscountPercent(),
            discountAmount.doubleValue(),
            finalCharge.doubleValue()
        );

        return rentalAgreementRepository.save(agreement);
    }

    private void validateCheckoutRequest(CheckoutRequest request) {
        if (request.getRentalDayCount() < 1) {
            throw new InvalidRentalDayCountException("Rental day count must be 1 or greater. Provided value: " + request.getRentalDayCount());
        }
        if (request.getDiscountPercent() < 0 || request.getDiscountPercent() > 100) {
            throw new InvalidDiscountPercentException("Discount percent must be between 0 and 100. Provided value: " + request.getDiscountPercent() + "%");
        }
    }

    private LocalDate calculateDueDate(LocalDate checkoutDate, int rentalDays) {
        return checkoutDate.plusDays(rentalDays);
    }

    private int calculateChargeDays(Tool tool, LocalDate checkoutDate, LocalDate dueDate) {
        int chargeDays = 0;
        LocalDate currentDate = checkoutDate.plusDays(1);  // Start from day after checkout
        while (!currentDate.isAfter(dueDate)) {
            if (isChargeableDay(tool, currentDate)) {
                chargeDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return chargeDays;
    }

    private boolean isChargeableDay(Tool tool, LocalDate date) {
        ToolType toolType = tool.getType();
        boolean isWeekend = date.getDayOfWeek().getValue() >= 6;
        boolean isHoliday = holidayRepository.findByDate(date) != null;

        if (isHoliday && !toolType.isHolidayCharge()) return false;
        if (isWeekend && !toolType.isWeekendCharge()) return false;
        return toolType.isWeekdayCharge() || isWeekend || isHoliday;
    }

    private BigDecimal calculatePreDiscountCharge(BigDecimal dailyCharge, int chargeDays) {
        return dailyCharge.multiply(BigDecimal.valueOf(chargeDays)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateDiscountAmount(BigDecimal preDiscountCharge, int discountPercent) {
        return preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public String printRentalAgreement(RentalAgreement agreement) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        StringBuilder sb = new StringBuilder();

        sb.append("Tool code: ").append(agreement.getTool().getCode()).append("\n");
        sb.append("Tool type: ").append(agreement.getTool().getType().getName()).append("\n");
        sb.append("Tool brand: ").append(agreement.getTool().getBrand()).append("\n");
        sb.append("Rental days: ").append(agreement.getRentalDays()).append("\n");
        sb.append("Check out date: ").append(agreement.getCheckoutDate().format(dateFormatter)).append("\n");
        sb.append("Due date: ").append(agreement.getDueDate().format(dateFormatter)).append("\n");
        sb.append("Daily rental charge: ").append(formatCurrency(agreement.getDailyRentalCharge())).append("\n");
        sb.append("Charge days: ").append(agreement.getChargeDays()).append("\n");
        sb.append("Pre-discount charge: ").append(formatCurrency(agreement.getPreDiscountCharge())).append("\n");
        sb.append("Discount percent: ").append(formatPercent(agreement.getDiscountPercent())).append("\n");
        sb.append("Discount amount: ").append(formatCurrency(agreement.getDiscountAmount())).append("\n");
        sb.append("Final charge: ").append(formatCurrency(agreement.getFinalCharge())).append("\n");

        return sb.toString();
    }

    private String formatCurrency(double amount) {
        return String.format("$%,.2f", amount);
    }

    private String formatPercent(int percent) {
        return String.format("%d%%", percent);
    }
}
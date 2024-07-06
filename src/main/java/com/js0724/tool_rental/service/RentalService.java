package com.js0724.tool_rental.service;

import com.js0724.tool_rental.dto.CheckoutRequest;
import com.js0724.tool_rental.exception.InvalidDiscountPercentException;
import com.js0724.tool_rental.exception.InvalidRentalDayCountException;
import com.js0724.tool_rental.model.Holiday;
import com.js0724.tool_rental.model.RentalAgreement;
import com.js0724.tool_rental.model.Tool;
import com.js0724.tool_rental.model.ToolType;
import com.js0724.tool_rental.repository.HolidayRepository;
import com.js0724.tool_rental.repository.RentalAgreementRepository;
import com.js0724.tool_rental.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public interface RentalService {
    RentalAgreement checkout(CheckoutRequest request);
    List<RentalAgreement> getAllRentalAgreements();
    Optional<RentalAgreement> getRentalAgreementById(Long id);
    RentalAgreement estimateRental(CheckoutRequest request);
    String printRentalAgreement(RentalAgreement agreement);
}

@Service
class RentalServiceImpl implements RentalService {
    private final ToolRepository toolRepository;
    private final HolidayRepository holidayRepository;
    private final RentalAgreementRepository rentalAgreementRepository;

    @Autowired
    public RentalServiceImpl(ToolRepository toolRepository, HolidayRepository holidayRepository, 
                             RentalAgreementRepository rentalAgreementRepository) {
        this.toolRepository = toolRepository;
        this.holidayRepository = holidayRepository;
        this.rentalAgreementRepository = rentalAgreementRepository;
    }

    @Override
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

    @Override
    public List<RentalAgreement> getAllRentalAgreements() {
        return rentalAgreementRepository.findAll();
    }

    @Override
    public Optional<RentalAgreement> getRentalAgreementById(Long id) {
        return rentalAgreementRepository.findById(id);
    }

    @Override
    public RentalAgreement estimateRental(CheckoutRequest request) {
        validateCheckoutRequest(request);

        Tool tool = toolRepository.findById(request.getToolCode())
                .orElseThrow(() -> new IllegalArgumentException("Tool not found"));

        LocalDate dueDate = calculateDueDate(request.getCheckoutDate(), request.getRentalDayCount());
        int chargeDays = calculateChargeDays(tool, request.getCheckoutDate(), dueDate);
        
        BigDecimal dailyCharge = BigDecimal.valueOf(tool.getType().getDailyCharge());
        BigDecimal preDiscountCharge = calculatePreDiscountCharge(dailyCharge, chargeDays);
        BigDecimal discountAmount = calculateDiscountAmount(preDiscountCharge, request.getDiscountPercent());
        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        return new RentalAgreement(
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
    }

    @Override
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

    private void validateCheckoutRequest(CheckoutRequest request) {
        if (request.getRentalDayCount() < 1) {
            throw new InvalidRentalDayCountException("Rental day count must be 1 or greater");
        }
        if (request.getDiscountPercent() < 0 || request.getDiscountPercent() > 100) {
            throw new InvalidDiscountPercentException("Discount percent must be between 0 and 100");
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
        Holiday holiday = holidayRepository.findByDate(date);
        boolean isHoliday = holiday != null;

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

    private String formatCurrency(double amount) {
        return String.format("$%,.2f", amount);
    }

    private String formatPercent(int percent) {
        return String.format("%d%%", percent);
    }
}
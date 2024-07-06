package com.js0724.tool_rental.dto;

import java.time.LocalDate;

public class CheckoutRequest {
    private String toolCode;
    private int rentalDayCount;
    private int discountPercent;
    private LocalDate checkoutDate;

    public CheckoutRequest(String toolCode, int rentalDayCount, int discountPercent, LocalDate checkoutDate) {
        this.toolCode = toolCode;
        this.rentalDayCount = rentalDayCount;
        this.discountPercent = discountPercent;
        this.checkoutDate = checkoutDate;
    }

    public String getToolCode() {
        return toolCode;
    }

    public int getRentalDayCount() {
        return rentalDayCount;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }
}
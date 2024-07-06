package com.js0724.tool_rental.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class RentalAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Tool tool;

    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private double dailyRentalCharge;
    private int chargeDays;
    private double preDiscountCharge;
    private int discountPercent;
    private double discountAmount;
    private double finalCharge;

    public RentalAgreement() {}

    public RentalAgreement(Tool tool, int rentalDays, LocalDate checkoutDate, LocalDate dueDate,
                           double dailyRentalCharge, int chargeDays, double preDiscountCharge,
                           int discountPercent, double discountAmount, double finalCharge) {
        this.tool = tool;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.dailyRentalCharge = dailyRentalCharge;
        this.chargeDays = chargeDays;
        this.preDiscountCharge = preDiscountCharge;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalCharge = finalCharge;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Tool getTool() { return tool; }
    public void setTool(Tool tool) { this.tool = tool; }
    public int getRentalDays() { return rentalDays; }
    public void setRentalDays(int rentalDays) { this.rentalDays = rentalDays; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
    public void setCheckoutDate(LocalDate checkoutDate) { this.checkoutDate = checkoutDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public double getDailyRentalCharge() { return dailyRentalCharge; }
    public void setDailyRentalCharge(double dailyRentalCharge) { this.dailyRentalCharge = dailyRentalCharge; }
    public int getChargeDays() { return chargeDays; }
    public void setChargeDays(int chargeDays) { this.chargeDays = chargeDays; }
    public double getPreDiscountCharge() { return preDiscountCharge; }
    public void setPreDiscountCharge(double preDiscountCharge) { this.preDiscountCharge = preDiscountCharge; }
    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }
    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    public double getFinalCharge() { return finalCharge; }
    public void setFinalCharge(double finalCharge) { this.finalCharge = finalCharge; }
}
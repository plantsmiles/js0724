package com.js0724.tool_rental.service;

import com.js0724.tool_rental.dto.CheckoutRequest;
import com.js0724.tool_rental.exception.InvalidDiscountPercentException;
import com.js0724.tool_rental.exception.InvalidRentalDayCountException;
import com.js0724.tool_rental.model.*;
import com.js0724.tool_rental.repository.RentalAgreementRepository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private ToolService toolService;

    @Mock
    private HolidayService holidayService;

    @Mock
    private RentalAgreementRepository rentalAgreementRepository;

    @InjectMocks
    private RentalService rentalService;

    private Tool jakr, ladw, chns, jakd;

    @BeforeEach
    void setUp() {
        ToolType jackhammer = new ToolType("JAKR", "Jackhammer", 2.99, true, false, false);
        ToolType ladder = new ToolType("LADW", "Ladder", 1.99, true, true, false);
        ToolType chainsaw = new ToolType("CHNS", "Chainsaw", 1.49, true, false, true);

        jakr = new Tool("JAKR", jackhammer, "Ridgid");
        ladw = new Tool("LADW", ladder, "Werner");
        chns = new Tool("CHNS", chainsaw, "Stihl");
        jakd = new Tool("JAKD", jackhammer, "DeWalt");
    }

    @Test
    void testCheckout_Test1() {
        CheckoutRequest request = new CheckoutRequest("JAKR", 5, 101, LocalDate.of(2015, 9, 3));
        assertThrows(InvalidDiscountPercentException.class, () -> rentalService.checkout(request));
    }

    @Test
    void testCheckout_Test2() {
        when(toolService.getToolByCode("LADW")).thenReturn(Optional.of(ladw));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("LADW", 3, 10, LocalDate.of(2020, 7, 2));
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(3, agreement.getChargeDays());
        assertEquals(5.97, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.60, agreement.getDiscountAmount(), 0.01);
        assertEquals(5.37, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testCheckout_Test3() {
        when(toolService.getToolByCode("CHNS")).thenReturn(Optional.of(chns));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("CHNS", 5, 25, LocalDate.of(2015, 7, 2));
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(3, agreement.getChargeDays());
        assertEquals(4.47, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(1.12, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.35, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testCheckout_Test4() {
        when(toolService.getToolByCode("JAKD")).thenReturn(Optional.of(jakd));
        when(holidayService.getAllHolidays()).thenReturn(Arrays.asList(
            new Holiday("Labor Day", LocalDate.of(2015, 9, 7), true)
        ));

        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("JAKD", 6, 0, LocalDate.of(2015, 9, 3));
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(3, agreement.getChargeDays());
        assertEquals(8.97, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(8.97, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testCheckout_Test5() {
        when(toolService.getToolByCode("JAKR")).thenReturn(Optional.of(jakr));
        when(holidayService.getAllHolidays()).thenReturn(Arrays.asList(
            new Holiday("Independence Day", LocalDate.of(2015, 7, 4), true)
        ));

        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("JAKR", 9, 0, LocalDate.of(2015, 7, 2));
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(5, agreement.getChargeDays());
        assertEquals(14.95, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(14.95, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testCheckout_Test6() {
        when(toolService.getToolByCode("JAKR")).thenReturn(Optional.of(jakr));
        when(holidayService.getAllHolidays()).thenReturn(Arrays.asList(
            new Holiday("Independence Day", LocalDate.of(2020, 7, 4), true)
        ));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("JAKR", 4, 50, LocalDate.of(2020, 7, 2));
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(1, agreement.getChargeDays());
        assertEquals(2.99, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(1.50, agreement.getDiscountAmount(), 0.01);
        assertEquals(1.49, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testCheckout_InvalidRentalDayCount() {
        CheckoutRequest request = new CheckoutRequest("JAKR", 0, 0, LocalDate.of(2015, 9, 3));
        assertThrows(InvalidRentalDayCountException.class, () -> rentalService.checkout(request));
    }

    @Test
    void testCheckout_NegativeDiscountPercent() {
        CheckoutRequest request = new CheckoutRequest("JAKR", 5, -10, LocalDate.of(2015, 9, 3));
        assertThrows(InvalidDiscountPercentException.class, () -> rentalService.checkout(request));
    }

    @Test
    void testCheckout_MaximumDiscountPercent() {
        when(toolService.getToolByCode("JAKR")).thenReturn(Optional.of(jakr));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("JAKR", 5, 100, LocalDate.of(2015, 9, 3));
        RentalAgreement agreement = rentalService.checkout(request);
        assertEquals(0, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testCheckout_OneDay() {
        when(toolService.getToolByCode("JAKR")).thenReturn(Optional.of(jakr));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("JAKR", 1, 0, LocalDate.of(2015, 9, 3));
        RentalAgreement agreement = rentalService.checkout(request);
        assertEquals(1, agreement.getChargeDays());
    }

    @Test
    void testCheckout_LongTermRental() {
        when(toolService.getToolByCode("JAKR")).thenReturn(Optional.of(jakr));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("JAKR", 100, 0, LocalDate.of(2015, 9, 3));
        RentalAgreement agreement = rentalService.checkout(request);
        
        assertTrue(agreement.getChargeDays() > 0);
        assertTrue(agreement.getFinalCharge() > 0);
    }

    @Test
    void testCheckout_ChainSawOverWeekend() {
        when(toolService.getToolByCode("CHNS")).thenReturn(Optional.of(chns));
        when(holidayService.getAllHolidays()).thenReturn(Arrays.asList(
            new Holiday("Independence Day", LocalDate.of(2015, 7, 4), true)
        ));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("CHNS", 3, 0, LocalDate.of(2015, 7, 3)); // Friday
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(1, agreement.getChargeDays()); // Only Friday should be charged
    }

    @Test
    void testCheckout_LadderOverHoliday() {
        when(toolService.getToolByCode("LADW")).thenReturn(Optional.of(ladw));
        when(holidayService.getAllHolidays()).thenReturn(Arrays.asList(
            new Holiday("Independence Day", LocalDate.of(2015, 7, 4), true)
        ));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("LADW", 3, 0, LocalDate.of(2015, 7, 2)); // Thursday, Friday (Holiday), Saturday
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(1, agreement.getChargeDays()); // All days should be charged for ladder
    }

    @Test
    void testCheckout_JackhammerOverWeekendAndHoliday() {
        when(toolService.getToolByCode("JAKD")).thenReturn(Optional.of(jakd));
        when(holidayService.getAllHolidays()).thenReturn(Arrays.asList(
            new Holiday("Independence Day", LocalDate.of(2015, 7, 4), true)
        ));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("JAKD", 4, 0, LocalDate.of(2015, 7, 3)); // Friday (Holiday), Saturday, Sunday, Monday
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(2, agreement.getChargeDays());
    }

    @Test
    void testCheckout_ToolNotFound() {
        String toolCode = "NONEXISTENT";
        when(toolService.getToolByCode(toolCode)).thenReturn(Optional.empty());

        CheckoutRequest request = new CheckoutRequest(toolCode, 1, 0, LocalDate.of(2015, 9, 3));
        assertThrows(IllegalArgumentException.class, () -> rentalService.checkout(request));
    }

    @Test
    void testPrintRentalAgreement() {
        when(toolService.getToolByCode("JAKR")).thenReturn(Optional.of(jakr));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutRequest request = new CheckoutRequest("JAKR", 5, 10, LocalDate.of(2015, 9, 3));
        RentalAgreement agreement = rentalService.checkout(request);
        String printedAgreement = rentalService.printRentalAgreement(agreement);
        
        assertTrue(printedAgreement.contains("Tool code: JAKR"));
        assertTrue(printedAgreement.contains("Tool type: Jackhammer"));
        assertTrue(printedAgreement.contains("Tool brand: Ridgid"));
        assertTrue(printedAgreement.contains("Rental days: 5"));
        assertTrue(printedAgreement.contains("Check out date: 09/03/15"));
        assertTrue(printedAgreement.contains("Due date: 09/08/15"));
        assertTrue(printedAgreement.contains("Daily rental charge: $2.99"));
        assertTrue(printedAgreement.contains("Charge days: 3"));
        assertTrue(printedAgreement.contains("Pre-discount charge: $8.97"));
        assertTrue(printedAgreement.contains("Discount percent: 10%"));
        assertTrue(printedAgreement.contains("Discount amount: $0.90"));
        assertTrue(printedAgreement.contains("Final charge: $8.07"));
    }
}
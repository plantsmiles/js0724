package com.js0724.tool_rental.util;

import java.time.LocalDate;
import java.time.Month;

public class DateUtils {

    private DateUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Finds the first Monday of a given month in a specific year.
     *
     * @param year The year
     * @param month The month
     * @return The day of the month that is the first Monday
     */
    public static int findFirstMondayOfMonth(int year, Month month) {
        LocalDate date = LocalDate.of(year, month, 1);
        while (date.getDayOfWeek().getValue() != 1) {
            date = date.plusDays(1);
        }
        return date.getDayOfMonth();
    }

    /**
     * Gets the current year.
     *
     * @return The current year
     */
    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }
}
package com.js0724.tool_rental.controller;

import com.js0724.tool_rental.model.Holiday;
import com.js0724.tool_rental.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    @Autowired
    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public List<Holiday> getAllHolidays() {
        return holidayService.getAllHolidays();
    }

    @GetMapping("/between")
    public List<Holiday> getHolidaysBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return holidayService.getHolidaysBetweenDates(startDate, endDate);
    }

    @GetMapping("/date")
    public ResponseEntity<Holiday> getHolidayByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Holiday holiday = holidayService.getHolidayByDate(date);
        if (holiday != null) {
            return ResponseEntity.ok(holiday);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
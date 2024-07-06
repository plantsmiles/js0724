package com.js0724.tool_rental.service;

import com.js0724.tool_rental.model.Holiday;
import com.js0724.tool_rental.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public interface HolidayService {
    List<Holiday> getAllHolidays();
    List<Holiday> getHolidaysBetweenDates(LocalDate startDate, LocalDate endDate);
    Holiday getHolidayByDate(LocalDate date);
}

@Service
class HolidayServiceImpl implements HolidayService {
    private final HolidayRepository holidayRepository;

    @Autowired
    public HolidayServiceImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    @Override
    public List<Holiday> getHolidaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        return holidayRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    public Holiday getHolidayByDate(LocalDate date) {
        return holidayRepository.findByDate(date);
    }
}
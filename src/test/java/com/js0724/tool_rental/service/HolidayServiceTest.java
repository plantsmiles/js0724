package com.js0724.tool_rental.service;

import com.js0724.tool_rental.model.Holiday;
import com.js0724.tool_rental.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HolidayServiceTest {

    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private HolidayServiceImpl holidayService;

    private final LocalDate startDate = LocalDate.of(2021, 1, 1);
    private final LocalDate endDate = LocalDate.of(2021, 12, 31);
    private final LocalDate specificDate = LocalDate.of(2021, 7, 4);
    private Holiday holiday;

    @BeforeEach
    void setUp() {
        holiday = new Holiday("Independence Day", specificDate, true);
        when(holidayRepository.findAll()).thenReturn(Arrays.asList(holiday));
        when(holidayRepository.findByDateBetween(startDate, endDate)).thenReturn(Arrays.asList(holiday));
        when(holidayRepository.findByDate(specificDate)).thenReturn(holiday);
    }

    @Test
    void testGetAllHolidays() {
        List<Holiday> holidays = holidayService.getAllHolidays();
        assertNotNull(holidays);
        assertFalse(holidays.isEmpty());
        assertEquals(1, holidays.size());
        assertEquals(holiday, holidays.get(0));
        verify(holidayRepository).findAll();
    }

    @Test
    void testGetHolidaysBetweenDates() {
        List<Holiday> holidays = holidayService.getHolidaysBetweenDates(startDate, endDate);
        assertNotNull(holidays);
        assertFalse(holidays.isEmpty());
        assertEquals(1, holidays.size());
        assertEquals(holiday, holidays.get(0));
        verify(holidayRepository).findByDateBetween(startDate, endDate);
    }

    @Test
    void testGetHolidayByDate() {
        Holiday result = holidayService.getHolidayByDate(specificDate);
        assertNotNull(result);
        assertEquals(holiday, result);
        verify(holidayRepository).findByDate(specificDate);
    }
}

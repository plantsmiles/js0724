package com.js0724.tool_rental.repository;

import com.js0724.tool_rental.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    
    // Find a holiday by its date
    Holiday findByDate(LocalDate date);
    
    // Find holidays within a date range
    List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find holidays by whether they're observed on weekdays
    List<Holiday> findByObservedOnWeekday(boolean observedOnWeekday);
}
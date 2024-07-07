package com.js0724.tool_rental.repository;

import com.js0724.tool_rental.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    Holiday findByDate(LocalDate date);
    List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Holiday> findByObservedOnWeekday(boolean observedOnWeekday);
}
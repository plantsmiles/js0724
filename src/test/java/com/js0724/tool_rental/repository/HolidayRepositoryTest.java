package com.js0724.tool_rental.repository;

import com.js0724.tool_rental.model.Holiday;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class HolidayRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    void whenFindByDate_thenReturnHoliday() {
        // given
        Holiday independenceDay = new Holiday("Independence Day", LocalDate.of(2023, 7, 4), true);
        entityManager.persist(independenceDay);
        entityManager.flush();

        // when
        Holiday found = holidayRepository.findByDate(LocalDate.of(2023, 7, 4));

        // then
        assertThat(found.getName()).isEqualTo(independenceDay.getName());
    }

    @Test
    void whenFindByDateBetween_thenReturnHolidayList() {
        // given
        Holiday independenceDay = new Holiday("Independence Day", LocalDate.of(2023, 7, 4), true);
        Holiday laborDay = new Holiday("Labor Day", LocalDate.of(2023, 9, 4), true);
        entityManager.persist(independenceDay);
        entityManager.persist(laborDay);
        entityManager.flush();

        // when
        List<Holiday> found = holidayRepository.findByDateBetween(
            LocalDate.of(2023, 7, 1), LocalDate.of(2023, 12, 31));

        // then
        assertThat(found).hasSize(2);
    }

    @Test
    void whenFindByObservedOnWeekday_thenReturnHolidayList() {
        // given
        Holiday independenceDay = new Holiday("Independence Day", LocalDate.of(2023, 7, 4), true);
        Holiday christmas = new Holiday("Christmas", LocalDate.of(2023, 12, 25), false);
        entityManager.persist(independenceDay);
        entityManager.persist(christmas);
        entityManager.flush();

        // when
        List<Holiday> found = holidayRepository.findByObservedOnWeekday(true);

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Independence Day");
    }
}
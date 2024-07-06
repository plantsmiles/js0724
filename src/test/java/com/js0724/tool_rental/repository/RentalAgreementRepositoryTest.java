package com.js0724.tool_rental.repository;

import com.js0724.tool_rental.model.RentalAgreement;
import com.example.toolrental.model.Tool;
import com.example.toolrental.model.ToolType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RentalAgreementRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;

    @Test
    void whenFindByTool_thenReturnRentalAgreementList() {
        // given
        ToolType ladderType = new ToolType("LADW", "Ladder", 1.99, true, true, false);
        Tool ladder = new Tool("LADW", ladderType, "Werner");
        entityManager.persist(ladderType);
        entityManager.persist(ladder);

        RentalAgreement agreement = new RentalAgreement(ladder, 3, LocalDate.now(), LocalDate.now().plusDays(3),
                1.99, 3, 5.97, 10, 0.60, 5.37);
        entityManager.persist(agreement);
        entityManager.flush();

        // when
        List<RentalAgreement> found = rentalAgreementRepository.findByTool(ladder);

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTool().getCode()).isEqualTo(ladder.getCode());
    }

    @Test
    void whenFindByCheckoutDate_thenReturnRentalAgreementList() {
        // given
        ToolType ladderType = new ToolType("LADW", "Ladder", 1.99, true, true, false);
        Tool ladder = new Tool("LADW", ladderType, "Werner");
        entityManager.persist(ladderType);
        entityManager.persist(ladder);

        LocalDate checkoutDate = LocalDate.now();
        RentalAgreement agreement = new RentalAgreement(ladder, 3, checkoutDate, checkoutDate.plusDays(3),
                1.99, 3, 5.97, 10, 0.60, 5.37);
        entityManager.persist(agreement);
        entityManager.flush();

        // when
        List<RentalAgreement> found = rentalAgreementRepository.findByCheckoutDate(checkoutDate);

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getCheckoutDate()).isEqualTo(checkoutDate);
    }

    @Test
    void whenFindByDiscountPercentGreaterThan_thenReturnRentalAgreementList() {
        // given
        ToolType ladderType = new ToolType("LADW", "Ladder", 1.99, true, true, false);
        Tool ladder = new Tool("LADW", ladderType, "Werner");
        entityManager.persist(ladderType);
        entityManager.persist(ladder);

        RentalAgreement agreement1 = new RentalAgreement(ladder, 3, LocalDate.now(), LocalDate.now().plusDays(3),
                1.99, 3, 5.97, 10, 0.60, 5.37);
        RentalAgreement agreement2 = new RentalAgreement(ladder, 3, LocalDate.now(), LocalDate.now().plusDays(3),
                1.99, 3, 5.97, 5, 0.30, 5.67);
        entityManager.persist(agreement1);
        entityManager.persist(agreement2);
        entityManager.flush();

        // when
        List<RentalAgreement> found = rentalAgreementRepository.findByDiscountPercentGreaterThan(7);

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getDiscountPercent()).isEqualTo(10);
    }
}
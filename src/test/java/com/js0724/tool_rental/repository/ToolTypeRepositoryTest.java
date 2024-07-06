package com.js0724.tool_rental.repository;

import com.js0724.tool_rental.model.ToolType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ToolTypeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ToolTypeRepository toolTypeRepository;

    @Test
    void whenFindByCode_thenReturnToolType() {
        // given
        ToolType ladderType = new ToolType("LADW", "Ladder", 1.99, true, true, false);
        entityManager.persist(ladderType);
        entityManager.flush();

        // when
        Optional<ToolType> found = toolTypeRepository.findById("LADW");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(ladderType.getName());
        assertThat(found.get().getDailyCharge()).isEqualTo(ladderType.getDailyCharge());
    }

    @Test
    void whenFindAll_thenReturnAllToolTypes() {
        // given
        ToolType ladderType = new ToolType("LADW", "Ladder", 1.99, true, true, false);
        ToolType chainsawType = new ToolType("CHNS", "Chainsaw", 1.49, true, false, true);
        entityManager.persist(ladderType);
        entityManager.persist(chainsawType);
        entityManager.flush();

        // when
        List<ToolType> found = toolTypeRepository.findAll();

        // then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(ToolType::getCode).containsExactlyInAnyOrder("LADW", "CHNS");
    }

    @Test
    void whenSave_thenPersistToolType() {
        // given
        ToolType jackhammer = new ToolType("JAKD", "Jackhammer", 2.99, true, false, false);

        // when
        ToolType saved = toolTypeRepository.save(jackhammer);

        // then
        ToolType found = entityManager.find(ToolType.class, "JAKD");
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo(jackhammer.getName());
        assertThat(found.getDailyCharge()).isEqualTo(jackhammer.getDailyCharge());
    }

    @Test
    void whenDelete_thenRemoveToolType() {
        // given
        ToolType ladderType = new ToolType("LADW", "Ladder", 1.99, true, true, false);
        entityManager.persist(ladderType);
        entityManager.flush();

        // when
        toolTypeRepository.deleteById("LADW");

        // then
        ToolType found = entityManager.find(ToolType.class, "LADW");
        assertThat(found).isNull();
    }

    @Test
    void whenUpdate_thenModifyToolType() {
        // given
        ToolType ladderType = new ToolType("LADW", "Ladder", 1.99, true, true, false);
        entityManager.persist(ladderType);
        entityManager.flush();

        // when
        ToolType toUpdate = toolTypeRepository.findById("LADW").orElseThrow();
        toUpdate.setDailyCharge(2.99);
        toolTypeRepository.save(toUpdate);

        // then
        ToolType updated = entityManager.find(ToolType.class, "LADW");
        assertThat(updated).isNotNull();
        assertThat(updated.getDailyCharge()).isEqualTo(2.99);
    }
}
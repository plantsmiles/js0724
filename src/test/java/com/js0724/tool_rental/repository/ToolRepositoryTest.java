package com.js0724.tool_rental.repository;

import com.js0724.tool_rental.model.Tool;
import com.example.toolrental.model.ToolType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ToolRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ToolRepository toolRepository;

    @Test
    void whenFindByCode_thenReturnTool() {
        // given
        ToolType ladderType = new ToolType("LADW", "Ladder", 1.99, true, true, false);
        entityManager.persist(ladderType);

        Tool ladder = new Tool("LADW", ladderType, "Werner");
        entityManager.persist(ladder);
        entityManager.flush();

        // when
        Tool found = toolRepository.findById("LADW").orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.code()).isEqualTo(ladder.code());
        assertThat(found.brand()).isEqualTo(ladder.brand());
        assertThat(found.type().name()).isEqualTo(ladder.type().name());
    }
}
package com.js0724.tool_rental;

import com.js0724.tool_rental.dto.CheckoutRequest;
import com.js0724.tool_rental.model.Holiday;
import com.js0724.tool_rental.model.Tool;
import com.js0724.tool_rental.model.ToolType;
import com.js0724.tool_rental.repository.HolidayRepository;
import com.js0724.tool_rental.repository.ToolRepository;
import com.js0724.tool_rental.repository.ToolTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("e2e")
@AutoConfigureMockMvc
public class ToolRentalE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ToolTypeRepository toolTypeRepository;

    @Autowired
    private HolidayRepository holidayRepository;

    @BeforeEach
    void setUp() {
        // Clear existing data
        holidayRepository.deleteAll();
        toolRepository.deleteAll();
        toolTypeRepository.deleteAll();

        // Set up tool types
        ToolType ladderType = toolTypeRepository.save(new ToolType("LADW", "Ladder", 1.99, true, true, false));
        ToolType chainsawType = toolTypeRepository.save(new ToolType("CHNS", "Chainsaw", 1.49, true, false, true));
        ToolType jackhammerType = toolTypeRepository.save(new ToolType("JAKD", "Jackhammer", 2.99, true, false, false));

        // Set up tools
        toolRepository.save(new Tool("LADW", ladderType, "Werner"));
        toolRepository.save(new Tool("CHNS", chainsawType, "Stihl"));
        toolRepository.save(new Tool("JAKD", jackhammerType, "DeWalt"));
        toolRepository.save(new Tool("JAKR", jackhammerType, "Ridgid"));

        // Set up holidays
        holidayRepository.save(new Holiday("Independence Day", LocalDate.of(2023, 7, 4), true));
        holidayRepository.save(new Holiday("Labor Day", LocalDate.of(2023, 9, 4), true));
    }

    @Test
    void completeRentalProcess() throws Exception {
        // Step 1: Get all tools
        mockMvc.perform(get("/api/tools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].code", is("LADW")));

        // Step 2: Get specific tool
        mockMvc.perform(get("/api/tools/CHNS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("CHNS")))
                .andExpect(jsonPath("$.brand", is("Stihl")));

        // Step 3: Get all holidays
        mockMvc.perform(get("/api/holidays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Independence Day")));

        // Step 4: Estimate rental
        CheckoutRequest estimateRequest = new CheckoutRequest("JAKR", 5, 10, LocalDate.of(2023, 7, 2));
        mockMvc.perform(get("/api/rentals/estimate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estimateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Tool code: JAKR")))
                .andExpect(content().string(containsString("Rental days: 5")))
                .andExpect(content().string(containsString("Check out date: 07/02/23")))
                .andExpect(content().string(containsString("Due date: 07/07/23")));

        // Step 5: Checkout
        CheckoutRequest checkoutRequest = new CheckoutRequest("LADW", 3, 10, LocalDate.of(2023, 7, 2));
        mockMvc.perform(post("/api/rentals/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(checkoutRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Tool code: LADW")))
                .andExpect(content().string(containsString("Tool type: Ladder")))
                .andExpect(content().string(containsString("Tool brand: Werner")))
                .andExpect(content().string(containsString("Rental days: 3")))
                .andExpect(content().string(containsString("Check out date: 07/02/23")))
                .andExpect(content().string(containsString("Due date: 07/05/23")))
                .andExpect(content().string(containsString("Daily rental charge: $1.99")))
                .andExpect(content().string(containsString("Charge days: 3")))
                .andExpect(content().string(containsString("Pre-discount charge: $5.97")))
                .andExpect(content().string(containsString("Discount percent: 10%")))
                .andExpect(content().string(containsString("Discount amount: $0.60")))
                .andExpect(content().string(containsString("Final charge: $5.37")));

        // Step 6: Get all rental agreements
        mockMvc.perform(get("/api/rentals/agreements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tool.code", is("LADW")));
    }
}
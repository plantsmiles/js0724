package com.js0724.tool_rental;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.js0724.tool_rental.dto.CheckoutRequest;
import com.js0724.tool_rental.model.Holiday;
import com.js0724.tool_rental.model.Tool;
import com.js0724.tool_rental.model.ToolType;
import com.js0724.tool_rental.repository.HolidayRepository;
import com.js0724.tool_rental.repository.RentalAgreementRepository;
import com.js0724.tool_rental.repository.ToolRepository;
import com.js0724.tool_rental.repository.ToolTypeRepository;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ToolRentalApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

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

	@Autowired
	private RentalAgreementRepository rentalAgreementRepository;

    @BeforeEach
    void setUp() {
        // Clear existing data, this isn't required since we are using a h2 in memory database, but could swap out for live postgress
        rentalAgreementRepository.deleteAll(); // has to be first, unless change Tool OneToMany to cascade delete
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
        holidayRepository.save(new Holiday("Labor Day", LocalDate.of(2023, 9, 4), false));
    }

    @Test
    void contextLoads() {
        // This test ensures that the Spring context can start up correctly
        assertNotNull(applicationContext);
    }

    @Test
    void mainComponentsAreLoaded() {
        // Ensure the "contract" of whats needed is present
        assertTrue(applicationContext.containsBean("toolRepository"));
		assertTrue(applicationContext.containsBean("toolServiceImpl"));
		assertTrue(applicationContext.containsBean("toolController"));

		assertTrue(applicationContext.containsBean("toolTypeRepository"));
		assertTrue(applicationContext.containsBean("toolTypeServiceImpl"));
		assertTrue(applicationContext.containsBean("toolTypeController"));

		assertTrue(applicationContext.containsBean("holidayRepository"));
        assertTrue(applicationContext.containsBean("holidayServiceImpl"));
		assertTrue(applicationContext.containsBean("holidayController"));

		assertTrue(applicationContext.containsBean("rentalService"));
		assertTrue(applicationContext.containsBean("rentalAgreementRepository"));
		assertTrue(applicationContext.containsBean("rentalController"));

		assertTrue(applicationContext.containsBean("homeController"));
    }

    @Test
    void databaseConnectionIsEstablished() {
        // Test that the database connection is established
        assertTrue(applicationContext.containsBean("dataSource"));
        assertDoesNotThrow(() -> applicationContext.getBean("entityManagerFactory"));
    }

    @Test
    void propertiesAreLoaded() {
        // Test that application properties are loaded correctly
        assertNotNull(applicationContext.getEnvironment().getProperty("spring.datasource.url"));
    }

	@Test
    void completeRentalProcess() throws Exception {
		// The goal here is just to functional test the entire rental process from start to finish as a smoke test
		// The unit tests in RentalService cover all the business logic variations
		// Also covers testing the controllers, as writing unit tests for them tests the framework more than our business logic

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
                .andExpect(content().string(containsString("Charge days: 2")))
                .andExpect(content().string(containsString("Pre-discount charge: $3.98")))
                .andExpect(content().string(containsString("Discount percent: 10%")))
                .andExpect(content().string(containsString("Discount amount: $0.40")))
                .andExpect(content().string(containsString("Final charge: $3.58")));

        // Step 6: Get all rental agreements
        mockMvc.perform(get("/api/rentals/agreements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tool.code", is("LADW")));
    }
}
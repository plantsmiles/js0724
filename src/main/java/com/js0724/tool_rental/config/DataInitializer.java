package com.js0724.tool_rental.config;

import com.js0724.tool_rental.model.Holiday;
import com.js0724.tool_rental.model.Tool;
import com.js0724.tool_rental.model.ToolType;
import com.js0724.tool_rental.repository.HolidayRepository;
import com.js0724.tool_rental.repository.ToolRepository;
import com.js0724.tool_rental.repository.ToolTypeRepository;
import com.js0724.tool_rental.util.DateUtils;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(ToolTypeRepository toolTypeRepository, ToolRepository toolRepository, HolidayRepository holidayRepository) {
        return args -> {
            //Initialize Tools
            ToolType ladder = new ToolType("LADW", "Ladder", 1.99, true, true, false);
            ToolType chainsaw = new ToolType("CHNS", "Chainsaw", 1.49, true, false, true);
            ToolType jackhammer = new ToolType("JAKD", "Jackhammer", 2.99, true, false, false);

            toolTypeRepository.save(ladder);
            toolTypeRepository.save(chainsaw);
            toolTypeRepository.save(jackhammer);

            Tool ladderWerner = new Tool("LADW", ladder, "Werner");
            Tool chainsawStihl = new Tool("CHNS", chainsaw, "Stihl");
            Tool jackhammerDeWalt = new Tool("JAKD", jackhammer, "DeWalt");
            Tool jackhammerRidgid = new Tool("JAKR", jackhammer, "Ridgid");

            toolRepository.save(ladderWerner);
            toolRepository.save(chainsawStihl);
            toolRepository.save(jackhammerDeWalt);
            toolRepository.save(jackhammerRidgid);

            // Initialize Holidays
            int currentYear = DateUtils.getCurrentYear();
            
            Holiday independenceDay = new Holiday(
                "Independence Day",
                LocalDate.of(currentYear, Month.JULY, 4),
                true
            );
            
            Holiday laborDay = new Holiday(
                "Labor Day",
                LocalDate.of(currentYear, Month.SEPTEMBER, 
                    DateUtils.findFirstMondayOfMonth(currentYear, Month.SEPTEMBER)),
                true
            );

            holidayRepository.save(independenceDay);
            holidayRepository.save(laborDay);
        };
    }
}
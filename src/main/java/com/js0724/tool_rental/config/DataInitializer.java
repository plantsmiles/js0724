package com.js0724.tool_rental.config;

import com.js0724.tool_rental.model.Tool;
import com.example.toolrental.model.ToolType;
import com.example.toolrental.repository.ToolRepository;
import com.example.toolrental.repository.ToolTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(ToolTypeRepository toolTypeRepository, ToolRepository toolRepository) {
        return args -> {
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
        };
    }
}
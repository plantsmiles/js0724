package com.js0724.tool_rental.repository;

import com.js0724.tool_rental.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolRepository extends JpaRepository<Tool, String> {}
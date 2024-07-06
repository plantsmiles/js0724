package com.js0724.tool_rental.service;

import com.js0724.tool_rental.model.Tool;
import com.js0724.tool_rental.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ToolService {
    List<Tool> getAllTools();
    Optional<Tool> getToolByCode(String code);
}

@Service
class ToolServiceImpl implements ToolService {
    private final ToolRepository toolRepository;

    @Autowired
    public ToolServiceImpl(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    @Override
    public List<Tool> getAllTools() {
        return toolRepository.findAll();
    }

    @Override
    public Optional<Tool> getToolByCode(String code) {
        return toolRepository.findById(code);
    }
}
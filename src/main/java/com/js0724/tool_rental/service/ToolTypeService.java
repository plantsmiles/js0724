package com.js0724.tool_rental.service;

import com.js0724.tool_rental.model.ToolType;
import com.js0724.tool_rental.repository.ToolTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ToolTypeService {
    List<ToolType> getAllToolTypes();
    Optional<ToolType> getToolTypeByCode(String code);
}

@Service
class ToolTypeServiceImpl implements ToolTypeService {
    private final ToolTypeRepository toolTypeRepository;

    @Autowired
    public ToolTypeServiceImpl(ToolTypeRepository toolTypeRepository) {
        this.toolTypeRepository = toolTypeRepository;
    }

    @Override
    public List<ToolType> getAllToolTypes() {
        return toolTypeRepository.findAll();
    }

    @Override
    public Optional<ToolType> getToolTypeByCode(String code) {
        return toolTypeRepository.findById(code);
    }
}
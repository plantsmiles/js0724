package com.js0724.tool_rental.controller;

import com.js0724.tool_rental.model.ToolType;
import com.js0724.tool_rental.service.ToolTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tool-types")
public class ToolTypeController {

    private final ToolTypeService toolTypeService;

    @Autowired
    public ToolTypeController(ToolTypeService toolTypeService) {
        this.toolTypeService = toolTypeService;
    }

    @GetMapping
    public List<ToolType> getAllToolTypes() {
        return toolTypeService.getAllToolTypes();
    }

    @GetMapping("/{code}")
    public ResponseEntity<ToolType> getToolTypeByCode(@PathVariable String code) {
        return toolTypeService.getToolTypeByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
package com.js0724.tool_rental.service;

import com.js0724.tool_rental.model.Tool;
import com.js0724.tool_rental.repository.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToolServiceTest {

    @Mock
    private ToolRepository toolRepository;

    @InjectMocks
    private ToolServiceImpl toolService;

    private Tool tool1;
    private Tool tool2;

    @BeforeEach
    void setUp() {
        tool1 = new Tool("TOOL1", null, "Brand1");
        tool2 = new Tool("TOOL2", null, "Brand2");

        when(toolRepository.findAll()).thenReturn(Arrays.asList(tool1, tool2));
        when(toolRepository.findById("TOOL1")).thenReturn(Optional.of(tool1));
        when(toolRepository.findById("TOOL2")).thenReturn(Optional.of(tool2));
        when(toolRepository.findById("NONEXISTENT")).thenReturn(Optional.empty());
    }

    @Test
    void testGetAllTools() {
        List<Tool> tools = toolService.getAllTools();
        assertNotNull(tools);
        assertEquals(2, tools.size());
        assertTrue(tools.contains(tool1));
        assertTrue(tools.contains(tool2));
        verify(toolRepository).findAll();
    }

    @Test
    void testGetToolByCode_ExistingTool() {
        Optional<Tool> foundTool = toolService.getToolByCode("TOOL1");
        assertTrue(foundTool.isPresent());
        assertEquals(tool1, foundTool.get());
        verify(toolRepository).findById("TOOL1");
    }

    @Test
    void testGetToolByCode_NonExistingTool() {
        Optional<Tool> foundTool = toolService.getToolByCode("NONEXISTENT");
        assertFalse(foundTool.isPresent());
        verify(toolRepository).findById("NONEXISTENT");
    }
}
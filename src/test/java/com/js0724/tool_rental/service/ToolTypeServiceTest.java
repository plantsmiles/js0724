package com.js0724.tool_rental.service;

import com.js0724.tool_rental.model.ToolType;
import com.js0724.tool_rental.repository.ToolTypeRepository;
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
public class ToolTypeServiceTest {

    @Mock
    private ToolTypeRepository toolTypeRepository;

    @InjectMocks
    private ToolTypeServiceImpl toolTypeService;

    private ToolType toolType1;
    private ToolType toolType2;

    @BeforeEach
    void setUp() {
        toolType1 = new ToolType("JAKR", "Jackhammer", 2.99, true, false, false);
        toolType2 = new ToolType("LADW", "Ladder", 1.99, true, true, false);

        when(toolTypeRepository.findAll()).thenReturn(Arrays.asList(toolType1, toolType2));
        when(toolTypeRepository.findById("JAKR")).thenReturn(Optional.of(toolType1));
        when(toolTypeRepository.findById("LADW")).thenReturn(Optional.of(toolType2));
        when(toolTypeRepository.findById("NONEXISTENT")).thenReturn(Optional.empty());
    }

    @Test
    void testGetAllToolTypes() {
        List<ToolType> result = toolTypeService.getAllToolTypes();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(toolType1));
        assertTrue(result.contains(toolType2));
        verify(toolTypeRepository).findAll();
    }

    @Test
    void testGetToolTypeByCode_ExistingToolType() {
        Optional<ToolType> result = toolTypeService.getToolTypeByCode("JAKR");
        assertTrue(result.isPresent());
        assertEquals(toolType1, result.get());
        verify(toolTypeRepository).findById("JAKR");
    }

    @Test
    void testGetToolTypeByCode_NonExistingToolType() {
        Optional<ToolType> result = toolTypeService.getToolTypeByCode("NONEXISTENT");
        assertFalse(result.isPresent());
        verify(toolTypeRepository).findById("NONEXISTENT");
    }
}
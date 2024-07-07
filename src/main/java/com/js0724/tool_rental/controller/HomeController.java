package com.js0724.tool_rental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    /**
     * This controller serves as a simple demonstration of Spring's MVC pattern.
     * A full-blown UI was considered out of scope for this phase of the project.
     * However, this setup illustrates that while we are currently using Spring MVC for simplicity,
     * the backend is designed in such a way that it could easily be integrated with a more robust
     * frontend framework like React, enhancing the user interface without major backend overhauls.
     */
    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("message", "A simple tool rental service API");

        return modelAndView;
    }
}
package com.js0724.tool_rental.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate date;
    private boolean observedOnWeekday;

    public Holiday() {}

    public Holiday(String name, LocalDate date, boolean observedOnWeekday) {
        this.name = name;
        this.date = date;
        this.observedOnWeekday = observedOnWeekday;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public boolean isObservedOnWeekday() { return observedOnWeekday; }
    public void setObservedOnWeekday(boolean observedOnWeekday) { this.observedOnWeekday = observedOnWeekday; }
}
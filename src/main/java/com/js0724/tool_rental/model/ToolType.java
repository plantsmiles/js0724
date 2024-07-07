package com.js0724.tool_rental.model;

import jakarta.persistence.*;

@Entity
public class ToolType {
    @Id
    private String code;
    private String name;
    private double dailyCharge;
    private boolean weekdayCharge;
    private boolean weekendCharge;
    private boolean holidayCharge;

    public ToolType() {}

    public ToolType(String code, String name, double dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        this.code = code;
        this.name = name;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getDailyCharge() { return dailyCharge; }
    public void setDailyCharge(double dailyCharge) { this.dailyCharge = dailyCharge; }
    public boolean isWeekdayCharge() { return weekdayCharge; }
    public void setWeekdayCharge(boolean weekdayCharge) { this.weekdayCharge = weekdayCharge; }
    public boolean isWeekendCharge() { return weekendCharge; }
    public void setWeekendCharge(boolean weekendCharge) { this.weekendCharge = weekendCharge; }
    public boolean isHolidayCharge() { return holidayCharge; }
    public void setHolidayCharge(boolean holidayCharge) { this.holidayCharge = holidayCharge; }
}
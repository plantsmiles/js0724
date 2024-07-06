package com.js0724.tool_rental.model;

import jakarta.persistence.*;

@Entity
public class Tool {
    @Id
    private String code;
    
    @ManyToOne
    private ToolType type;
    private String brand;

    public Tool() {}

    public Tool(String code, ToolType type, String brand) {
        this.code = code;
        this.type = type;
        this.brand = brand;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public ToolType getType() { return type; }
    public void setType(ToolType type) { this.type = type; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
}
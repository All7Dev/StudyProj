package com.example.SmartHouse.dto;

import com.example.SmartHouse.entity.SensorType;

public class SensorCreateDto {
    private SensorType type;
    private Double value;

    public SensorType getType() { return type; }
    public void setType(SensorType type) { this.type = type; }
    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
}
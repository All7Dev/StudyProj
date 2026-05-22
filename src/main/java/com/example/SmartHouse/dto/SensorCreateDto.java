package com.example.SmartHouse.dto;

import com.example.SmartHouse.enums.SensorType;

public record SensorCreateDto(SensorType type, Double value) {}
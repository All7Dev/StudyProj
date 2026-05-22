package com.example.SmartHouse.dto;

import com.example.SmartHouse.enums.DeviceType;

public record DeviceCreateDto(String name, DeviceType type, Boolean isOn, Integer value) {}
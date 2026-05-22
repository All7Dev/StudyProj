package com.example.SmartHouse.dto;

public record ScenarioCreateDto(String name, String type, Double targetTemp, Integer targetLight, Boolean turnOnMusic) {}
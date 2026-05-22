package com.example.SmartHouse.dto;

import java.util.List;

public record ScenarioImportDto(List<ScenarioYaml> scenarios) {
    public record ScenarioYaml(String name, String type, Double targetTemp, Integer targetLight, Boolean turnOnMusic) {}
}
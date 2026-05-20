package com.example.SmartHouse.dto;

public class ScenarioCreateDto {
    private String name;
    private String type;   // "AUTO", "MANUAL", "ECO"
    private Double targetTemp;
    private Integer targetLight;
    private Boolean turnOnMusic;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getTargetTemp() { return targetTemp; }
    public void setTargetTemp(Double targetTemp) { this.targetTemp = targetTemp; }
    public Integer getTargetLight() { return targetLight; }
    public void setTargetLight(Integer targetLight) { this.targetLight = targetLight; }
    public Boolean getTurnOnMusic() { return turnOnMusic; }
    public void setTurnOnMusic(Boolean turnOnMusic) { this.turnOnMusic = turnOnMusic; }
}
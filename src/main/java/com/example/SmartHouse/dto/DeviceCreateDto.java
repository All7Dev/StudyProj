package com.example.SmartHouse.dto;

import com.example.SmartHouse.entity.DeviceType;

public class DeviceCreateDto {
    private String name;
    private DeviceType type;
    private Boolean isOn;
    private Integer value;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public DeviceType getType() { return type; }
    public void setType(DeviceType type) { this.type = type; }
    public Boolean getIsOn() { return isOn; }
    public void setIsOn(Boolean isOn) { this.isOn = isOn; }
    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }
}
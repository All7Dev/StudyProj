package com.example.SmartHouse.entity;

import com.example.SmartHouse.enums.DeviceType;
import jakarta.persistence.*;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private DeviceType type;

    private Boolean isOn = false;
    private Integer value;

    public Device() {}

    public Device(String name, DeviceType type) {
        this.name = name;
        this.type = type;
        this.isOn = false;
        this.value = null;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public DeviceType getType() { return type; }
    public void setType(DeviceType type) { this.type = type; }

    public Boolean getIsOn() { return isOn; }
    public void setIsOn(Boolean isOn) { this.isOn = isOn; }

    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }
}
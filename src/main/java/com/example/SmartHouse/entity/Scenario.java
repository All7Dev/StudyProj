package com.example.SmartHouse.entity;

import com.example.SmartHouse.enums.ScenarioType;
import jakarta.persistence.*;

@Entity
@Table(name = "scenarios")
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ScenarioType type;

    private Double targetTemp;
    private Integer targetLight;
    private Boolean turnOnMusic;

    public Scenario() {}

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ScenarioType getType() { return type; }
    public void setType(ScenarioType type) { this.type = type; }

    public Double getTargetTemp() { return targetTemp; }
    public void setTargetTemp(Double targetTemp) { this.targetTemp = targetTemp; }

    public Integer getTargetLight() { return targetLight; }
    public void setTargetLight(Integer targetLight) { this.targetLight = targetLight; }

    public Boolean getTurnOnMusic() { return turnOnMusic; }
    public void setTurnOnMusic(Boolean turnOnMusic) { this.turnOnMusic = turnOnMusic; }
}
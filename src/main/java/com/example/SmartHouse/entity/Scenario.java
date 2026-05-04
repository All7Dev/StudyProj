package com.example.SmartHouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "scenarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;           // "Утро", "Вечер", "Отъезд"

    @Enumerated(EnumType.STRING)
    private ScenarioType type;     // AUTO, MANUAL, ECO

    private Double targetTemp;     // целевая температура
    private Integer targetLight;   // целевая яркость (0-100)
    private Boolean turnOnMusic;   // включить музыку?
}
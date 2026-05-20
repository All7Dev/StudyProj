package com.example.SmartHouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SensorType type;

    private Double value;

    private LocalDateTime timestamp = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private SensorStatus status = SensorStatus.OK;

    // конструкторы
    public Sensor() {}

    public Sensor(SensorType type, Double value) {
        this.type = type;
        this.value = value;
        this.timestamp = LocalDateTime.now();
        this.status = SensorStatus.OK;
    }

    // геттеры и сеттеры (Lombok не работает – пишем вручную)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SensorType getType() { return type; }
    public void setType(SensorType type) { this.type = type; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public SensorStatus getStatus() { return status; }
    public void setStatus(SensorStatus status) { this.status = status; }
}
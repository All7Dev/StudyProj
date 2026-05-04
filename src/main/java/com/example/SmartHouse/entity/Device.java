package com.example.SmartHouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;          // "Умная лампочка"

    @Enumerated(EnumType.STRING) // хранить в БД как строку ("LAMP", "AC", ...)
    private DeviceType type;      // LAMP, AC, SPEAKER, HUMIDIFIER

    private Boolean isOn = false; // включено/выключено

    private Integer value;        // яркость (0-100) или температура (16-30) или громкость

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    // Для удобства: конструктор без id (если нужно)
    public Device(String name, DeviceType type, Room room) {
        this.name = name;
        this.type = type;
        this.room = room;
        this.isOn = false;
        this.value = null;
    }
}
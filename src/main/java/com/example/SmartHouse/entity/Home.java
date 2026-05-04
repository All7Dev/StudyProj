package com.example.SmartHouse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity                     // Эта аннотация говорит: "Этот класс будет таблицей в БД"
@Table(name = "homes")      // Имя таблицы в базе данных
@Getter                     // Lombok: автоматически генерирует геттеры для всех полей
@Setter                     // Lombok: автоматически генерирует сеттеры
@NoArgsConstructor          // Конструктор без параметров (нужен JPA)
@AllArgsConstructor         // Конструктор со всеми параметрами
public class Home {

    @Id                     // Поле - первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоинкремент (serial)
    private Long id;

    @Column(nullable = false)  // Поле NOT NULL в БД
    private String name;        // Например "Моя квартира"

    private String address;     // Адрес (может быть null)

    // Связь "один ко многим": один дом содержит много комнат
    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    // Удобные методы для добавления/удаления комнаты
    public void addRoom(Room room) {
        rooms.add(room);
        room.setHome(this);
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
        room.setHome(null);
    }
}
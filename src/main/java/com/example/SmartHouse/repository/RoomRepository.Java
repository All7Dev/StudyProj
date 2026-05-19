package com.example.SmartHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartHouse.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    // Репозиторий готов. Все базовые методы уже есть.
}
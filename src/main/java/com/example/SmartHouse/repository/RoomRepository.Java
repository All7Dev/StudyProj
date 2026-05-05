package com.example.SmartHouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartHouse.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
        // Этот метод позволит найти все комнаты по ID дома
    List<Room> findByHomeId(Long homeId);
}
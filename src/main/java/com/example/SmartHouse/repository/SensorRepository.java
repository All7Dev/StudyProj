package com.example.SmartHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartHouse.entity.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
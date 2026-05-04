package com.example.SmartHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartHouse.entity.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
}
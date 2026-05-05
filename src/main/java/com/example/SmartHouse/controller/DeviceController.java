package com.example.SmartHouse.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartHouse.entity.Device;
import com.example.SmartHouse.repository.DeviceRepository;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    // Создать устройство
    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        Device saved = deviceRepository.save(device);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Получить все устройства
    @GetMapping
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    // Получить устройство по id
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        Optional<Device> device = deviceRepository.findById(id);
        return device.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Обновить устройство
    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device deviceDetails) {
        Optional<Device> optionalDevice = deviceRepository.findById(id);
        if (optionalDevice.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Device existingDevice = optionalDevice.get();
        existingDevice.setName(deviceDetails.getName());
        existingDevice.setType(deviceDetails.getType());
        existingDevice.setIsOn(deviceDetails.getIsOn());
        existingDevice.setValue(deviceDetails.getValue());
        existingDevice.setRoom(deviceDetails.getRoom());
        Device updated = deviceRepository.save(existingDevice);
        return ResponseEntity.ok(updated);
    }

    // Удалить устройство
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        if (!deviceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        deviceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
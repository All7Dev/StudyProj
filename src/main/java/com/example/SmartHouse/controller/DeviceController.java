package com.example.SmartHouse.controller;

import com.example.SmartHouse.dto.DeviceCreateDto;
import com.example.SmartHouse.entity.Device;
import com.example.SmartHouse.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody DeviceCreateDto dto) {
        return new ResponseEntity<>(deviceService.createDevice(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        return deviceService.getDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody DeviceCreateDto dto) {
        return deviceService.updateDevice(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        return deviceService.deleteDevice(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
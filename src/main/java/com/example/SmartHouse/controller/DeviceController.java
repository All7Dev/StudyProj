package com.example.SmartHouse.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.example.SmartHouse.dto.DeviceCreateDto;
import com.example.SmartHouse.entity.Device;
import com.example.SmartHouse.repository.DeviceRepository;
import com.example.SmartHouse.util.TelegramService;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private TelegramService telegramService;

    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody DeviceCreateDto dto) {
        log.info("Запрос на создание устройства: {}", dto.getName());
        Device device = new Device();
        device.setName(dto.getName());
        device.setType(dto.getType());
        device.setIsOn(dto.getIsOn() != null ? dto.getIsOn() : false);
        device.setValue(dto.getValue());
        Device saved = deviceRepository.save(device);
        log.info("Устройство создано с id: {}", saved.getId());
        telegramService.sendMessage("Новое устройство: " + saved.getName());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    

    @GetMapping
    public List<Device> getAllDevices() {
        log.info("Запрос на список всех устройств");
        return deviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        log.info("Запрос на устройство id={}", id);
        Optional<Device> device = deviceRepository.findById(id);
        return device.map(ResponseEntity::ok).orElseGet(() -> {
            log.warn("Устройство id={} не найдено", id);
            return ResponseEntity.notFound().build();
        });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device deviceDetails) {
        log.info("Запрос на обновление устройства id={}", id);
        Optional<Device> optional = deviceRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Устройство id={} не найдено для обновления", id);
            return ResponseEntity.notFound().build();
        }
        Device existing = optional.get();
        existing.setName(deviceDetails.getName());
        existing.setType(deviceDetails.getType());
        existing.setIsOn(deviceDetails.getIsOn());
        existing.setValue(deviceDetails.getValue());
        Device updated = deviceRepository.save(existing);
        log.info("Устройство обновлено: {}", updated.getName());
        telegramService.sendMessage("Устройство обновлено: " + updated.getName());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        log.info("Запрос на удаление устройства id={}", id);
        if (!deviceRepository.existsById(id)) {
            log.warn("Устройство id={} не найдено для удаления", id);
            return ResponseEntity.notFound().build();
        }
        deviceRepository.deleteById(id);
        log.info("Устройство id={} удалено", id);
        telegramService.sendMessage("Устройство удалено, id=" + id);
        return ResponseEntity.noContent().build();
    }
}
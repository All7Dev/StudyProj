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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartHouse.dto.SensorCreateDto;
import com.example.SmartHouse.entity.Sensor;
import com.example.SmartHouse.repository.SensorRepository;
import com.example.SmartHouse.service.FaultSimulationService;
import com.example.SmartHouse.util.TelegramService;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private FaultSimulationService faultService;

    private static final Logger log = LoggerFactory.getLogger(SensorController.class);

    @PostMapping
    public ResponseEntity<Sensor> createSensor(@RequestBody SensorCreateDto dto) {
        log.info("Запрос на создание датчика типа {}", dto.getType());
        Sensor sensor = new Sensor();
        sensor.setType(dto.getType());
        sensor.setValue(dto.getValue());
        Sensor saved = sensorRepository.save(sensor);
        log.info("Датчик создан с id={}", saved.getId());
        telegramService.sendMessage("Новый датчик: " + saved.getType());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Sensor> getAllSensors() {
        log.info("Запрос списка всех датчиков");
        return sensorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Long id) {
        log.info("Запрос датчика id={}", id);
        Optional<Sensor> sensor = sensorRepository.findById(id);
        return sensor.map(ResponseEntity::ok).orElseGet(() -> {
            log.warn("Датчик id={} не найден", id);
            return ResponseEntity.notFound().build();
        });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sensor> updateSensor(@PathVariable Long id, @RequestBody Sensor sensorDetails) {
        log.info("Запрос на обновление датчика id={}", id);
        Optional<Sensor> optional = sensorRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Датчик id={} не найден для обновления", id);
            return ResponseEntity.notFound().build();
        }
        Sensor existing = optional.get();
        existing.setType(sensorDetails.getType());
        existing.setValue(sensorDetails.getValue());
        existing.setTimestamp(sensorDetails.getTimestamp());
        existing.setStatus(sensorDetails.getStatus());
        Sensor updated = sensorRepository.save(existing);
        log.info("Датчик id={} обновлён", updated.getId());
        telegramService.sendMessage("Датчик обновлён, id=" + updated.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        log.info("Запрос на удаление датчика id={}", id);
        if (!sensorRepository.existsById(id)) {
            log.warn("Датчик id={} не найден для удаления", id);
            return ResponseEntity.notFound().build();
        }
        sensorRepository.deleteById(id);
        log.info("Датчик id={} удалён", id);
        telegramService.sendMessage("Датчик удалён, id=" + id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/simulate/{id}/fault")
    public ResponseEntity<?> simulateFault(@PathVariable Long id, @RequestParam String errorMessage) {
        log.info("Симуляция неисправности датчика id={}, сообщение: {}", id, errorMessage);
        faultService.simulateFault(id, errorMessage);
        return ResponseEntity.ok("Fault simulated and CSV report generated");
    }
}
package com.example.SmartHouse.controller;

import com.example.SmartHouse.dto.SensorCreateDto;
import com.example.SmartHouse.entity.Sensor;
import com.example.SmartHouse.service.FaultSimulationService;
import com.example.SmartHouse.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private FaultSimulationService faultSimulationService;

    @PostMapping
    public ResponseEntity<Sensor> createSensor(@RequestBody SensorCreateDto dto) {
        return new ResponseEntity<>(sensorService.createSensor(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Sensor> getAllSensors() {
        return sensorService.getAllSensors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Long id) {
        return sensorService.getSensorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sensor> updateSensor(@PathVariable Long id, @RequestBody SensorCreateDto dto) {
        return sensorService.updateSensor(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        return sensorService.deleteSensor(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/simulate/{id}/fault")
    public ResponseEntity<?> simulateFault(@PathVariable Long id, @RequestParam String errorMessage) {
        faultSimulationService.simulateFault(id, errorMessage);
        return ResponseEntity.ok("Fault simulated and CSV report generated");
    }
}
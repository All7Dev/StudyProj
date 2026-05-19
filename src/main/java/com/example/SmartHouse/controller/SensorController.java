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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartHouse.entity.Sensor;
import com.example.SmartHouse.repository.SensorRepository;
import com.example.SmartHouse.service.FaultSimulationService;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    @Autowired
    private SensorRepository sensorRepository;

    @PostMapping
    public ResponseEntity<Sensor> createSensor(@RequestBody Sensor sensor) {
        Sensor saved = sensorRepository.save(sensor);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Long id) {
        Optional<Sensor> sensor = sensorRepository.findById(id);
        return sensor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sensor> updateSensor(@PathVariable Long id, @RequestBody Sensor sensorDetails) {
        Optional<Sensor> optionalSensor = sensorRepository.findById(id);
        if (optionalSensor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Sensor existing = optionalSensor.get();
        existing.setType(sensorDetails.getType());
        existing.setValue(sensorDetails.getValue());
        existing.setTimestamp(sensorDetails.getTimestamp());
        existing.setRoom(sensorDetails.getRoom());
        Sensor updated = sensorRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        if (!sensorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sensorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private FaultSimulationService faultSimulationService;

    @PostMapping("/simulate/{id}/fault")
    public ResponseEntity<?> simulateFault(@PathVariable Long id, @RequestParam String errorMessage) {
        faultSimulationService.simulateFault(id, errorMessage);
        return ResponseEntity.ok("Fault simulated and CSV report generated");
}
}
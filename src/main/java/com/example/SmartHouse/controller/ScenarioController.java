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

import com.example.SmartHouse.entity.Scenario;
import com.example.SmartHouse.repository.ScenarioRepository;

@RestController
@RequestMapping("/api/scenarios")
public class ScenarioController {

    @Autowired
    private ScenarioRepository scenarioRepository;

    @PostMapping
    public ResponseEntity<Scenario> createScenario(@RequestBody Scenario scenario) {
        Scenario saved = scenarioRepository.save(scenario);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Scenario> getAllScenarios() {
        return scenarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scenario> getScenarioById(@PathVariable Long id) {
        Optional<Scenario> scenario = scenarioRepository.findById(id);
        return scenario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Scenario> updateScenario(@PathVariable Long id, @RequestBody Scenario scenarioDetails) {
        Optional<Scenario> optional = scenarioRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Scenario existing = optional.get();
        existing.setName(scenarioDetails.getName());
        existing.setType(scenarioDetails.getType());
        existing.setTargetTemp(scenarioDetails.getTargetTemp());
        existing.setTargetLight(scenarioDetails.getTargetLight());
        existing.setTurnOnMusic(scenarioDetails.getTurnOnMusic());
        Scenario updated = scenarioRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScenario(@PathVariable Long id) {
        if (!scenarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        scenarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
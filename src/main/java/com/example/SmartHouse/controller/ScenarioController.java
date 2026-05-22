package com.example.SmartHouse.controller;

import com.example.SmartHouse.dto.ScenarioCreateDto;
import com.example.SmartHouse.entity.Scenario;
import com.example.SmartHouse.service.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/scenarios")
public class ScenarioController {

    @Autowired
    private ScenarioService scenarioService;

    @PostMapping
    public ResponseEntity<Scenario> createScenario(@RequestBody ScenarioCreateDto dto) {
        return new ResponseEntity<>(scenarioService.createScenario(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Scenario> getAllScenarios() {
        return scenarioService.getAllScenarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scenario> getScenarioById(@PathVariable Long id) {
        return scenarioService.getScenarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Scenario> updateScenario(@PathVariable Long id, @RequestBody ScenarioCreateDto dto) {
        return scenarioService.updateScenario(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScenario(@PathVariable Long id) {
        return scenarioService.deleteScenario(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/import")
    public ResponseEntity<?> importScenariosFromYaml(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Файл пуст");
        }
        try {
            int count = scenarioService.importScenariosFromYaml(file);
            return ResponseEntity.ok("Импортировано " + count + " сценариев");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при импорте: " + e.getMessage());
        }
    }

    @GetMapping("/import-url")
    public ResponseEntity<String> getUploadPageUrl() {
        return ResponseEntity.ok("Страница импорта: /upload.html");
    }
}
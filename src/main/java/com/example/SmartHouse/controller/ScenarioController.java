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
import org.springframework.web.multipart.MultipartFile;

import com.example.SmartHouse.dto.ScenarioImportDto;
import com.example.SmartHouse.entity.Scenario;
import com.example.SmartHouse.entity.ScenarioType;
import com.example.SmartHouse.repository.ScenarioRepository;
import com.example.SmartHouse.service.FaultSimulationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.swagger.v3.oas.annotations.Operation;
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
    //Добавление эдпойнта для загрузки файлов
    @PostMapping("/import")
    public ResponseEntity<?> importScenariosFromYaml(@RequestParam("file") MultipartFile file) 
    {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Файл пуст");
        }
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            ScenarioImportDto importDto = mapper.readValue(file.getInputStream(), ScenarioImportDto.class);

            for (ScenarioImportDto.ScenarioYaml yamlScenario : importDto.getScenarios()) {
                Scenario scenario = new Scenario();
                scenario.setName(yamlScenario.getName());
                // Преобразование строки в enum
                scenario.setType(ScenarioType.valueOf(yamlScenario.getType()));
                scenario.setTargetTemp(yamlScenario.getTargetTemp());
                scenario.setTargetLight(yamlScenario.getTargetLight());
                scenario.setTurnOnMusic(yamlScenario.getTurnOnMusic());
                scenarioRepository.save(scenario);
            }

        return ResponseEntity.ok("Импортировано " + importDto.getScenarios().size() + " сценариев");
        } catch (Exception e) {
        return ResponseEntity.status(500).body("Ошибка при импорте: " + e.getMessage());
        }
    }
    @Operation(summary = "Получить ссылку на HTML-форму для импорта сценариев")
    @GetMapping("/import-url")
        public String getImportFormUrl() {
        return "http://localhost:8080/upload.html";
    }
    @Autowired
    private FaultSimulationService faultService;

    @PostMapping("/simulate/{id}/fault")
    public ResponseEntity<?> simulateFault(@PathVariable Long id, @RequestParam String errorMessage) {
        faultService.simulateFault(id, errorMessage);
        return ResponseEntity.ok("Fault simulated and report generated");
    }
}
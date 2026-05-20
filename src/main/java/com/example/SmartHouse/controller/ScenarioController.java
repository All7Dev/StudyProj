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
import org.springframework.web.multipart.MultipartFile;

import com.example.SmartHouse.dto.ScenarioCreateDto;
import com.example.SmartHouse.dto.ScenarioImportDto;
import com.example.SmartHouse.entity.Scenario;
import com.example.SmartHouse.entity.ScenarioType;
import com.example.SmartHouse.repository.ScenarioRepository;
import com.example.SmartHouse.util.TelegramService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@RestController
@RequestMapping("/api/scenarios")
public class ScenarioController {

    @Autowired
    private ScenarioRepository scenarioRepository;

    @Autowired
    private TelegramService telegramService;

    private static final Logger log = LoggerFactory.getLogger(ScenarioController.class);

    // 1. Создание сценария через DTO
    @PostMapping
    public ResponseEntity<Scenario> createScenario(@RequestBody ScenarioCreateDto dto) {
        log.info("Запрос на создание сценария: {}", dto.getName());
        Scenario scenario = new Scenario();
        scenario.setName(dto.getName());
        // Преобразуем строку в enum ScenarioType
        scenario.setType(ScenarioType.valueOf(dto.getType()));
        scenario.setTargetTemp(dto.getTargetTemp());
        scenario.setTargetLight(dto.getTargetLight());
        scenario.setTurnOnMusic(dto.getTurnOnMusic());
        Scenario saved = scenarioRepository.save(scenario);
        log.info("Сценарий создан с id: {}", saved.getId());
        telegramService.sendMessage("Новый сценарий: " + saved.getName());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // 2. Получить все сценарии
    @GetMapping
    public List<Scenario> getAllScenarios() {
        log.info("Запрос списка всех сценариев");
        return scenarioRepository.findAll();
    }

    // 3. Получить сценарий по id
    @GetMapping("/{id}")
    public ResponseEntity<Scenario> getScenarioById(@PathVariable Long id) {
        log.info("Запрос сценария id={}", id);
        Optional<Scenario> scenario = scenarioRepository.findById(id);
        return scenario.map(ResponseEntity::ok).orElseGet(() -> {
            log.warn("Сценарий id={} не найден", id);
            return ResponseEntity.notFound().build();
        });
    }

    // 4. Обновить сценарий
    @PutMapping("/{id}")
    public ResponseEntity<Scenario> updateScenario(@PathVariable Long id, @RequestBody ScenarioCreateDto dto) {
        log.info("Запрос на обновление сценария id={}", id);
        Optional<Scenario> optional = scenarioRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Сценарий id={} не найден для обновления", id);
            return ResponseEntity.notFound().build();
        }
        Scenario existing = optional.get();
        existing.setName(dto.getName());
        existing.setType(ScenarioType.valueOf(dto.getType()));
        existing.setTargetTemp(dto.getTargetTemp());
        existing.setTargetLight(dto.getTargetLight());
        existing.setTurnOnMusic(dto.getTurnOnMusic());
        Scenario updated = scenarioRepository.save(existing);
        log.info("Сценарий id={} обновлён", updated.getId());
        telegramService.sendMessage("Сценарий обновлён: " + updated.getName());
        return ResponseEntity.ok(updated);
    }

    // 5. Удалить сценарий
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScenario(@PathVariable Long id) {
        log.info("Запрос на удаление сценария id={}", id);
        if (!scenarioRepository.existsById(id)) {
            log.warn("Сценарий id={} не найден для удаления", id);
            return ResponseEntity.notFound().build();
        }
        scenarioRepository.deleteById(id);
        log.info("Сценарий id={} удалён", id);
        telegramService.sendMessage("Сценарий удалён, id=" + id);
        return ResponseEntity.noContent().build();
    }

    // 6. Импорт сценариев из YAML-файла
    @PostMapping("/import")
    public ResponseEntity<?> importScenariosFromYaml(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Файл пуст");
        }
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            ScenarioImportDto importDto = mapper.readValue(file.getInputStream(), ScenarioImportDto.class);
            int count = 0;
            for (var yamlScenario : importDto.getScenarios()) {
                Scenario scenario = new Scenario();
                scenario.setName(yamlScenario.getName());
                scenario.setType(ScenarioType.valueOf(yamlScenario.getType()));
                scenario.setTargetTemp(yamlScenario.getTargetTemp());
                scenario.setTargetLight(yamlScenario.getTargetLight());
                scenario.setTurnOnMusic(yamlScenario.getTurnOnMusic());
                scenarioRepository.save(scenario);
                count++;
            }
            log.info("Импортировано {} сценариев", count);
            telegramService.sendMessage("Импортировано сценариев: " + count);
            return ResponseEntity.ok("Импортировано " + count + " сценариев");
        } catch (Exception e) {
            log.error("Ошибка импорта: {}", e.getMessage());
            return ResponseEntity.status(500).body("Ошибка при импорте: " + e.getMessage());
        }
    }

    // 7. Ссылка на страницу загрузки (для Swagger)
    @GetMapping("/import-url")
    public ResponseEntity<String> getUploadPageUrl() {
        return ResponseEntity.ok("Страница импорта: /upload.html");
    }
}
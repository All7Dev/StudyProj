package com.example.SmartHouse.service;

import com.example.SmartHouse.dto.ScenarioCreateDto;
import com.example.SmartHouse.dto.ScenarioImportDto;
import com.example.SmartHouse.entity.Scenario;
import com.example.SmartHouse.enums.ScenarioType;
import com.example.SmartHouse.repository.ScenarioRepository;
import com.example.SmartHouse.util.TelegramService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ScenarioService {

    @Autowired
    private ScenarioRepository scenarioRepository;

    @Autowired
    private TelegramService telegramService;

    private static final Logger log = LoggerFactory.getLogger(ScenarioService.class);

    public Scenario createScenario(ScenarioCreateDto dto) {
        log.info("Создание сценария: {}", dto.name());
        Scenario scenario = new Scenario();
        scenario.setName(dto.name());
        scenario.setType(ScenarioType.valueOf(dto.type()));
        scenario.setTargetTemp(dto.targetTemp());
        scenario.setTargetLight(dto.targetLight());
        scenario.setTurnOnMusic(dto.turnOnMusic());
        Scenario saved = scenarioRepository.save(scenario);
        log.info("Сценарий создан с id: {}", saved.getId());
        telegramService.sendMessage("Новый сценарий: " + saved.getName());
        return saved;
    }

    public List<Scenario> getAllScenarios() {
        log.info("Запрос списка всех сценариев");
        return scenarioRepository.findAll();
    }

    public Optional<Scenario> getScenarioById(Long id) {
        log.info("Запрос сценария id={}", id);
        return scenarioRepository.findById(id);
    }

    public Optional<Scenario> updateScenario(Long id, ScenarioCreateDto dto) {
        log.info("Запрос на обновление сценария id={}", id);
        return scenarioRepository.findById(id).map(existing -> {
            existing.setName(dto.name());
            existing.setType(ScenarioType.valueOf(dto.type()));
            existing.setTargetTemp(dto.targetTemp());
            existing.setTargetLight(dto.targetLight());
            existing.setTurnOnMusic(dto.turnOnMusic());
            Scenario updated = scenarioRepository.save(existing);
            log.info("Сценарий id={} обновлён", updated.getId());
            telegramService.sendMessage("Сценарий обновлён: " + updated.getName());
            return updated;
        });
    }

    public boolean deleteScenario(Long id) {
        log.info("Запрос на удаление сценария id={}", id);
        if (scenarioRepository.existsById(id)) {
            scenarioRepository.deleteById(id);
            log.info("Сценарий id={} удалён", id);
            telegramService.sendMessage("Сценарий удалён, id=" + id);
            return true;
        } else {
            log.warn("Сценарий id={} не найден для удаления", id);
            return false;
        }
    }

    public int importScenariosFromYaml(MultipartFile file) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ScenarioImportDto importDto = mapper.readValue(file.getInputStream(), ScenarioImportDto.class);
        int count = 0;
        for (var yaml : importDto.scenarios()) {
            Scenario scenario = new Scenario();
            scenario.setName(yaml.name());
            scenario.setType(ScenarioType.valueOf(yaml.type()));
            scenario.setTargetTemp(yaml.targetTemp());
            scenario.setTargetLight(yaml.targetLight());
            scenario.setTurnOnMusic(yaml.turnOnMusic());
            scenarioRepository.save(scenario);
            count++;
        }
        log.info("Импортировано {} сценариев", count);
        telegramService.sendMessage("Импортировано сценариев: " + count);
        return count;
    }
}
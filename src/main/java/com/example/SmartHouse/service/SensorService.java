package com.example.SmartHouse.service;

import com.example.SmartHouse.dto.SensorCreateDto;
import com.example.SmartHouse.entity.Sensor;
import com.example.SmartHouse.enums.SensorStatus;
import com.example.SmartHouse.repository.SensorRepository;
import com.example.SmartHouse.util.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private TelegramService telegramService;

    private static final Logger log = LoggerFactory.getLogger(SensorService.class);

    public Sensor createSensor(SensorCreateDto dto) {
        log.info("Создание датчика типа {}", dto.type());
        Sensor sensor = new Sensor();
        sensor.setType(dto.type());
        sensor.setValue(dto.value());
        sensor.setTimestamp(LocalDateTime.now());
        sensor.setStatus(SensorStatus.OK);
        Sensor saved = sensorRepository.save(sensor);
        log.info("Датчик создан с id={}", saved.getId());
        telegramService.sendMessage("Новый датчик: " + saved.getType());
        return saved;
    }

    public List<Sensor> getAllSensors() {
        log.info("Запрос списка всех датчиков");
        return sensorRepository.findAll();
    }

    public Optional<Sensor> getSensorById(Long id) {
        log.info("Запрос датчика id={}", id);
        return sensorRepository.findById(id);
    }

    public Optional<Sensor> updateSensor(Long id, SensorCreateDto dto) {
        log.info("Запрос на обновление датчика id={}", id);
        return sensorRepository.findById(id).map(existing -> {
            existing.setType(dto.type());
            existing.setValue(dto.value());
            // timestamp обычно не обновляем, но можно установить existing.setTimestamp(LocalDateTime.now());
            Sensor updated = sensorRepository.save(existing);
            log.info("Датчик id={} обновлён", updated.getId());
            telegramService.sendMessage("Датчик обновлён: id=" + updated.getId());
            return updated;
        });
    }

    public boolean deleteSensor(Long id) {
        log.info("Запрос на удаление датчика id={}", id);
        if (sensorRepository.existsById(id)) {
            sensorRepository.deleteById(id);
            log.info("Датчик id={} удалён", id);
            telegramService.sendMessage("Датчик удалён, id=" + id);
            return true;
        } else {
            log.warn("Датчик id={} не найден для удаления", id);
            return false;
        }
    }

    // Метод для симуляции неисправности (можно оставить здесь, вызывать из контроллера)
    public void simulateFault(Long id, String errorMessage) {
        sensorRepository.findById(id).ifPresent(sensor -> {
            sensor.setStatus(SensorStatus.ERROR);
            sensorRepository.save(sensor);
            // Генерация отчёта о неисправности (вынесена в отдельный сервис FaultSimulationService)
        });
    }
}
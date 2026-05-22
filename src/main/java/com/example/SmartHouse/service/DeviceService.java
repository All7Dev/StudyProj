package com.example.SmartHouse.service;

import com.example.SmartHouse.dto.DeviceCreateDto;
import com.example.SmartHouse.entity.Device;
import com.example.SmartHouse.repository.DeviceRepository;
import com.example.SmartHouse.util.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private TelegramService telegramService;

    private static final Logger log = LoggerFactory.getLogger(DeviceService.class);

    public Device createDevice(DeviceCreateDto dto) {
        log.info("Создание устройства: {}", dto.name());
        Device device = new Device();
        device.setName(dto.name());
        device.setType(dto.type());
        device.setIsOn(dto.isOn() != null ? dto.isOn() : false);
        device.setValue(dto.value());
        Device saved = deviceRepository.save(device);
        log.info("Устройство создано с id={}", saved.getId());
        telegramService.sendMessage("Новое устройство: " + saved.getName());
        return saved;
    }

    public List<Device> getAllDevices() {
        log.info("Запрос списка всех устройств");
        return deviceRepository.findAll();
    }

    public Optional<Device> getDeviceById(Long id) {
        log.info("Запрос устройства id={}", id);
        return deviceRepository.findById(id);
    }

    public Optional<Device> updateDevice(Long id, DeviceCreateDto dto) {
        log.info("Запрос на обновление устройства id={}", id);
        return deviceRepository.findById(id).map(existing -> {
            existing.setName(dto.name());
            existing.setType(dto.type());
            existing.setIsOn(dto.isOn() != null ? dto.isOn() : false);
            existing.setValue(dto.value());
            Device updated = deviceRepository.save(existing);
            log.info("Устройство обновлено: {}", updated.getName());
            telegramService.sendMessage("Устройство обновлено: " + updated.getName());
            return updated;
        });
    }

    public boolean deleteDevice(Long id) {
        log.info("Запрос на удаление устройства id={}", id);
        if (deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
            log.info("Устройство id={} удалено", id);
            telegramService.sendMessage("Устройство удалено, id=" + id);
            return true;
        } else {
            log.warn("Устройство id={} не найдено для удаления", id);
            return false;
        }
    }
}
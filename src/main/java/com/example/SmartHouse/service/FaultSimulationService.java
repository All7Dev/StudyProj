package com.example.SmartHouse.service;

import com.example.SmartHouse.entity.Sensor;
import com.example.SmartHouse.entity.SensorStatus;
import com.example.SmartHouse.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FaultSimulationService {

    @Autowired
    private SensorRepository sensorRepository;

    private static final String REPORT_DIR = "reports/";

    public void simulateFault(Long sensorId, String errorMessage) {
        Sensor sensor = sensorRepository.findById(sensorId).orElse(null);
        if (sensor == null) return;
        sensor.setStatus(SensorStatus.ERROR);
        sensorRepository.save(sensor);
        generateFaultReport(sensor, errorMessage);
    }

    private void generateFaultReport(Sensor sensor, String errorMessage) {
        String solution = getSolutionForSensor(sensor.getType());
        String fileName = REPORT_DIR + "fault_" + sensor.getId() + "_" + System.currentTimeMillis() + ".csv";
        try {
            Path dir = Paths.get(REPORT_DIR);
            if (!Files.exists(dir)) Files.createDirectories(dir);
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("Timestamp,Sensor ID,Type,Problem,Solution\n");
                writer.write(String.format("%s,%d,%s,\"%s\",\"%s\"\n",
                        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        sensor.getId(),
                        sensor.getType(),
                        errorMessage,
                        solution));
            }
            System.out.println("Fault report generated: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getSolutionForSensor(com.example.SmartHouse.entity.SensorType type) {
        switch (type) {
            case TEMP: return "Проверьте охлаждение / нагреватель";
            case HUMIDITY: return "Калибровка датчика влажности";
            case CO2: return "Замените датчик CO2";
            case LIGHT: return "Проверьте питание освещения";
            case MOTION: return "Проверьте инфракрасный датчик";
            default: return "Обратитесь в сервисный центр";
        }
    }
}
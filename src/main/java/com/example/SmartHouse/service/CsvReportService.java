package com.example.SmartHouse.service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SmartHouse.entity.Sensor;
import com.example.SmartHouse.repository.SensorRepository;

@Service
public class CsvReportService {

    @Autowired
    private SensorRepository sensorRepository;

    private static final String REPORT_DIR = "reports/";

    public void generateSensorReport() {
        List<Sensor> sensors = sensorRepository.findAll();
        String fileName = REPORT_DIR + "sensors_report_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        try {
            Path dir = Paths.get(REPORT_DIR);
            if (!Files.exists(dir)) Files.createDirectories(dir);
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("ID,Type,Value,Timestamp,Status\n");
                for (Sensor s : sensors) {
                    writer.write(String.format("%d,%s,%.2f,%s,%s\n",
                            s.getId(),
                            s.getType(),
                            s.getValue(),
                            s.getTimestamp().toString(),
                            s.getStatus()));
                }
            }
            System.out.println("CSV report generated: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
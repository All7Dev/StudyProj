package com.example.SmartHouse.controller;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartHouse.entity.Sensor;
import com.example.SmartHouse.repository.SensorRepository;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private SensorRepository sensorRepository;

    // Отчёт по датчикам в формате CSV
    @GetMapping("/sensors/csv")
    public ResponseEntity<byte[]> exportSensorsToCsv() {
        List<Sensor> sensors = sensorRepository.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // Заголовки
        writer.println("ID,Type,Value,Timestamp,RoomId");

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        for (Sensor s : sensors) {
            String timestamp = s.getTimestamp() != null ? s.getTimestamp().format(formatter) : "";
            Long roomId = (s.getRoom() != null) ? s.getRoom().getId() : 0L;
            writer.printf("%d,%s,%.2f,%s,%d%n",
                    s.getId(),
                    s.getType(),
                    s.getValue(),
                    timestamp,
                    roomId);
        }
        writer.flush();

        byte[] csvBytes = out.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sensors_report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBytes);
    }
}
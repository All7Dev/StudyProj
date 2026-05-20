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

import com.example.SmartHouse.entity.Device;
import com.example.SmartHouse.entity.Sensor;
import com.example.SmartHouse.repository.DeviceRepository;
import com.example.SmartHouse.repository.SensorRepository;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping("/sensors/csv")
    public ResponseEntity<byte[]> exportSensorsToCsv() {
        List<Sensor> sensors = sensorRepository.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        writer.println("ID,Type,Value,Timestamp,Status");
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        for (Sensor s : sensors) {
            String timestamp = s.getTimestamp() != null ? s.getTimestamp().format(formatter) : "";
            writer.printf("%d,%s,%.2f,%s,%s%n",
                    s.getId(),
                    s.getType(),
                    s.getValue(),
                    timestamp,
                    s.getStatus());
        }
        writer.flush();

        byte[] csvBytes = out.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sensors_report.csv");

        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }

    @GetMapping("/devices/csv")
    public ResponseEntity<byte[]> exportDevicesToCsv() {
        List<Device> devices = deviceRepository.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        writer.println("ID,Name,Type,IsOn,Value");
        for (Device d : devices) {
            writer.printf("%d,%s,%s,%b,%d%n",
                    d.getId(),
                    d.getName(),
                    d.getType(),
                    d.getIsOn(),
                    d.getValue() != null ? d.getValue() : 0);
        }
        writer.flush();
        byte[] csvBytes = out.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=devices_report.csv");
        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }
}
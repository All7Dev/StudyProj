package com.example.SmartHouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class ReportScheduler {

    @Autowired
    private CsvReportService csvReportService;

    @Scheduled(fixedDelay = 1800000) // каждые 30 минут
    public void scheduledCsvReport() {
        csvReportService.generateSensorReport();
        System.out.println("CSV report generated at " + new java.util.Date());
    }
}
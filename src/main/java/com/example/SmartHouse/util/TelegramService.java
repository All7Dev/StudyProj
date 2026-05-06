package com.example.SmartHouse.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.chat-id}")
    private String chatId;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String message) {
        if (botToken == null || botToken.isEmpty() || chatId == null || chatId.isEmpty()) {
            System.err.println("Telegram bot token or chat id is not configured");
            return;
        }
        String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                botToken, chatId, message);
        try {
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.err.println("Failed to send telegram message: " + e.getMessage());
        }
    }
}
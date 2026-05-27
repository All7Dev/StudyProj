package com.example.SmartHouse.dto;

public record LoginResponse(
        boolean isLogged,
        String roles
) {
}

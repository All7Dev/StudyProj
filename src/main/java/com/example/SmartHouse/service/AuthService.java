package com.example.SmartHouse.service;

import org.springframework.http.ResponseEntity;

import com.example.SmartHouse.dto.LoginRequest;
import com.example.SmartHouse.dto.LoginResponse;
import com.example.SmartHouse.dto.UserLoggedDto;

public interface AuthService {
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken);

    ResponseEntity<LoginResponse> refresh(String refreshToken);

    ResponseEntity<LoginResponse> logout(String accessToken, String refreshToken);

    UserLoggedDto getUserLoggedInfo();
}
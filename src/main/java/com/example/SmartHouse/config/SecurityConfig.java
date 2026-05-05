package com.example.SmartHouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()          // разрешить всё
            )
            .csrf(csrf -> csrf.disable())          // отключить CSRF (для тестов)
            .formLogin(form -> form.disable())     // убрать страницу логина
            .httpBasic(basic -> basic.disable());  // убрать Basic авторизацию
        return http.build();
    }
}

/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()   // РАЗРЕШИТЬ ВСЕ ЗАПРОСЫ (без авторизации)
            )
            .csrf(csrf -> csrf.disable());  // отключаем CSRF для удобства тестирования API
        return http.build();
    }
}
     */
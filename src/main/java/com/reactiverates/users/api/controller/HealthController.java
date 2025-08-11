package com.reactiverates.users.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Здоровье", description = "API для проверки состояния приложения")
public class HealthController {

    @GetMapping
    @Operation(summary = "Проверить состояние приложения")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = Map.of(
                "status", "UP",
                "service", "Reactive Rates Users API",
                "version", "1.0.0",
                "timestamp", System.currentTimeMillis()
        );
        return ResponseEntity.ok(response);
    }
}

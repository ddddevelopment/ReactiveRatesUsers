package com.reactiverates.users.api.controller;

import com.reactiverates.users.infrastructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Аутентификация", description = "API для работы с JWT токенами")
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/validate")
    @Operation(summary = "Валидация JWT токена", description = "Проверка JWT токена и извлечение информации")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody TokenValidationRequest request) {
        try {
            if (!jwtService.validateToken(request.getToken())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Недействительный токен"));
            }
            
            String username = jwtService.extractUsername(request.getToken());
            List<String> roles = jwtService.extractRoles(request.getToken());
            List<String> authorities = jwtService.extractAuthorities(request.getToken());
            String tokenType = jwtService.extractTokenType(request.getToken());
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("username", username);
            response.put("roles", roles);
            response.put("authorities", authorities);
            response.put("tokenType", tokenType);
            response.put("message", "Токен действителен");
            
            log.info("Токен валидирован для пользователя: {} с ролями: {} и authorities: {}", 
                    username, roles, authorities);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Ошибка валидации токена: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибка валидации токена: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Информация о текущем пользователе", description = "Получение информации о текущем аутентифицированном пользователе")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "Пользователь не аутентифицирован"));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("username", authentication.getName());
            response.put("authorities", authentication.getAuthorities().stream()
                    .map(Object::toString)
                    .toList());
            response.put("authenticated", authentication.isAuthenticated());
            response.put("principal", authentication.getPrincipal());
            
            log.info("Информация о текущем пользователе: {} с authorities: {}", 
                    authentication.getName(), authentication.getAuthorities());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Ошибка получения информации о пользователе: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибка получения информации о пользователе: " + e.getMessage()));
        }
    }

    @GetMapping("/debug")
    @Operation(summary = "Отладочная информация", description = "Подробная информация о текущем пользователе и его authorities")
    public ResponseEntity<Map<String, Object>> debugUser() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "Пользователь не аутентифицирован"));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("username", authentication.getName());
            response.put("authenticated", authentication.isAuthenticated());
            response.put("principal", authentication.getPrincipal());
            response.put("authorities", authentication.getAuthorities().stream()
                    .map(Object::toString)
                    .toList());
            response.put("authoritiesCount", authentication.getAuthorities().size());
            response.put("hasRoleUser", authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
            response.put("hasRoleAdmin", authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
            response.put("hasRoleModerator", authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_MODERATOR")));
            
            log.info("Отладочная информация о пользователе: {} с authorities: {}", 
                    authentication.getName(), authentication.getAuthorities());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Ошибка получения отладочной информации: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибка получения отладочной информации: " + e.getMessage()));
        }
    }

    public static class TokenValidationRequest {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}




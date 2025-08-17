package com.reactiverates.users.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class TestAuthController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> userEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Доступ разрешен для пользователей с ролью USER");
        response.put("user", auth.getName());
        response.put("authorities", auth.getAuthorities());
        log.info("User endpoint accessed by: {} with authorities: {}", auth.getName(), auth.getAuthorities());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/moderator")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Map<String, Object>> moderatorEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Доступ разрешен для пользователей с ролью MODERATOR");
        response.put("user", auth.getName());
        response.put("authorities", auth.getAuthorities());
        log.info("Moderator endpoint accessed by: {} with authorities: {}", auth.getName(), auth.getAuthorities());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> adminEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Доступ разрешен для пользователей с ролью ADMIN");
        response.put("user", auth.getName());
        response.put("authorities", auth.getAuthorities());
        log.info("Admin endpoint accessed by: {} with authorities: {}", auth.getName(), auth.getAuthorities());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> authInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", auth.isAuthenticated());
        response.put("user", auth.getName());
        response.put("authorities", auth.getAuthorities());
        response.put("principal", auth.getPrincipal());
        log.info("Auth info requested by: {} with authorities: {}", auth.getName(), auth.getAuthorities());
        return ResponseEntity.ok(response);
    }
}

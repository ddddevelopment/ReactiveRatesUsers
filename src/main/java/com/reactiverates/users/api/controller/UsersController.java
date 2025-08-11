package com.reactiverates.users.api.controller;

import com.reactiverates.users.domain.model.CreateUserRequest;
import com.reactiverates.users.domain.model.UpdateUserRequest;
import com.reactiverates.users.domain.model.User;
import com.reactiverates.users.domain.model.UserDto;
import com.reactiverates.users.domain.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Пользователи", description = "API для управления пользователями")
public class UsersController {
    
    private final UsersService service;
    
    @GetMapping
    @Operation(summary = "Получить всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) {
        Optional<UserDto> user = service.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    @Operation(summary = "Получить пользователя по username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<UserDto> getUserByUsername(
            @Parameter(description = "Имя пользователя", example = "john_doe")
            @PathVariable String username) {
        Optional<UserDto> user = service.getUserByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Получить пользователя по email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<UserDto> getUserByEmail(
            @Parameter(description = "Email пользователя", example = "john@example.com")
            @PathVariable String email) {
        Optional<UserDto> user = service.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/role/{role}")
    @Operation(summary = "Получить пользователей по роли")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи успешно получены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<UserDto>> getUsersByRole(
            @Parameter(description = "Роль пользователя", example = "USER")
            @PathVariable User.UserRole role) {
        List<UserDto> users = service.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Получить активных пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Активные пользователи успешно получены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<UserDto>> getActiveUsers() {
        List<UserDto> users = service.getActiveUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Поиск пользователей по запросу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи успешно найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<UserDto>> searchUsers(
            @Parameter(description = "Поисковый запрос", example = "john")
            @RequestParam String q) {
        List<UserDto> users = service.searchUsers(q);
        return ResponseEntity.ok(users);
    }
    
    @PostMapping
    @Operation(summary = "Создать нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<UserDto> createUser(
            @Parameter(description = "Данные для создания пользователя")
            @Valid @RequestBody CreateUserRequest request) {
        try {
            UserDto createdUser = service.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Данные для обновления пользователя")
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            Optional<UserDto> updatedUser = service.updateUser(id, request);
            return updatedUser.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) {
        boolean deleted = service.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Деактивировать пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно деактивирован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Void> deactivateUser(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) {
        boolean deactivated = service.deactivateUser(id);
        return deactivated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Активировать пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно активирован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Void> activateUser(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) {
        boolean activated = service.activateUser(id);
        return activated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
package com.reactiverates.users.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на обновление пользователя")
public record UpdateUserRequest(
    @Schema(description = "Имя пользователя", example = "john_doe")
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    String username,
    
    @Schema(description = "Email пользователя", example = "john@example.com")
    @Email(message = "Некорректный формат email")
    String email,
    
    @Schema(description = "Пароль", example = "password123")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    String password,
    
    @Schema(description = "Имя", example = "John")
    String firstName,
    
    @Schema(description = "Фамилия", example = "Doe")
    String lastName,
    
    @Schema(description = "Номер телефона", example = "+7 (999) 123-45-67")
    String phoneNumber,
    
    @Schema(description = "Роль пользователя", example = "USER")
    User.UserRole role,
    
    @Schema(description = "Статус активности", example = "true")
    Boolean isActive
) {
    
    public User toDomain() {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .role(role)
                .isActive(isActive)
                .build();
    }
}

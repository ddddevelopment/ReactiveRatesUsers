package com.reactiverates.users.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель пользователя")
public class User {
    
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;
    
    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;
    
    @Schema(description = "Email пользователя", example = "john@example.com")
    private String email;
    
    @Schema(description = "Пароль пользователя")
    private String password;
    
    @Schema(description = "Имя", example = "John")
    private String firstName;
    
    @Schema(description = "Фамилия", example = "Doe")
    private String lastName;
    
    @Schema(description = "Номер телефона", example = "+7 (999) 123-45-67")
    private String phoneNumber;
    
    @Schema(description = "Роль пользователя", example = "USER")
    private UserRole role;
    
    @Schema(description = "Статус активности", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Дата создания", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Дата последнего обновления", example = "2024-01-01T10:00:00")
    private LocalDateTime updatedAt;
    
    @Schema(description = "Роли пользователей")
    public enum UserRole {
        @Schema(description = "Администратор")
        ADMIN,
        @Schema(description = "Обычный пользователь")
        USER,
        @Schema(description = "Модератор")
        MODERATOR
    }
    
    public boolean isActive() {
        return isActive != null && isActive;
    }
    
    public boolean isAdmin() {
        return UserRole.ADMIN.equals(role);
    }
    
    public boolean isModerator() {
        return UserRole.MODERATOR.equals(role);
    }
    
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return username;
    }
    
    public void activate() {
        this.isActive = true;
    }
    
    public void deactivate() {
        this.isActive = false;
    }
    
    public void changeRole(UserRole newRole) {
        this.role = newRole;
    }
}

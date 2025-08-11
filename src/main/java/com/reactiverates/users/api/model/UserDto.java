package com.reactiverates.users.api.model;

import com.reactiverates.users.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO пользователя")
public record UserDto(
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    Long id,
    
    @Schema(description = "Имя пользователя", example = "john_doe")
    String username,
    
    @Schema(description = "Email пользователя", example = "john@example.com")
    String email,
    
    @Schema(description = "Имя", example = "John")
    String firstName,
    
    @Schema(description = "Фамилия", example = "Doe")
    String lastName,
    
    @Schema(description = "Номер телефона", example = "+7 (999) 123-45-67")
    String phoneNumber,
    
    @Schema(description = "Роль пользователя", example = "USER")
    User.UserRole role,
    
    @Schema(description = "Статус активности", example = "true")
    Boolean isActive,
    
    @Schema(description = "Дата создания", example = "2024-01-01T10:00:00")
    LocalDateTime createdAt,
    
    @Schema(description = "Дата последнего обновления", example = "2024-01-01T10:00:00")
    LocalDateTime updatedAt
) {
    
    public static UserDto fromDomain(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getIsActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
    
    public User toDomain() {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .role(role)
                .isActive(isActive)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}

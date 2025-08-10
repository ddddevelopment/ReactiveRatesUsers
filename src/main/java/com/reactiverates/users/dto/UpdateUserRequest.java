package com.reactiverates.users.dto;

import com.reactiverates.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    private String username;
    
    @Email(message = "Некорректный формат email")
    private String email;
    
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;
    
    private String firstName;
    
    private String lastName;
    
    private String phoneNumber;
    
    private User.UserRole role;
    
    private Boolean isActive;
    
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

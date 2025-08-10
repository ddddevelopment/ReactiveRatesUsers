package com.reactiverates.users.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private Long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserRole role;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum UserRole {
        ADMIN, USER, MODERATOR
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

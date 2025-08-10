package com.reactiverates.users.domain.exception;

public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(Long id) {
        super("Пользователь с ID " + id + " не найден");
    }
    
    public UserNotFoundException(String field, String value) {
        super("Пользователь с " + field + " '" + value + "' не найден");
    }
}

package com.reactiverates.users.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    public UserAlreadyExistsException(String field, String value) {
        super("Пользователь с " + field + " '" + value + "' уже существует");
    }
}

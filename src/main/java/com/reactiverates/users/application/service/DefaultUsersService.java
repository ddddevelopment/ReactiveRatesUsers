package com.reactiverates.users.application.service;

import com.reactiverates.users.api.model.CreateUserRequest;
import com.reactiverates.users.api.model.UpdateUserRequest;
import com.reactiverates.users.api.model.UserDto;
import com.reactiverates.users.domain.model.User;
import com.reactiverates.users.domain.service.UsersService;
import com.reactiverates.users.infrastructure.persistence.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultUsersService implements UsersService {
    
    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;
    
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<UserDto> getUserById(Long id) {
        return repository.findById(id)
                .map(UserDto::fromUser);
    }
    
    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        return repository.findByUsername(username)
                .map(UserDto::fromUser);
    }
    
    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(UserDto::fromUser);
    }
    
    @Override
    public List<UserDto> getUsersByRole(User.UserRole role) {
        return repository.findByRole(role).stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserDto> getActiveUsers() {
        return repository.findByIsActive(true).stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserDto> searchUsers(String searchTerm) {
        return repository.findBySearchTerm(searchTerm).stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }
    
    @Override
    public UserDto createUser(CreateUserRequest request) {
        // Проверяем уникальность username и email
        if (repository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .isActive(true)
                .build();
        
        User savedUser = repository.save(user);
        return UserDto.fromUser(savedUser);
    }
    
    @Override
    public Optional<UserDto> updateUser(Long id, UpdateUserRequest request) {
        return repository.findById(id)
                .map(user -> {
                    if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
                        if (repository.existsByUsername(request.getUsername())) {
                            throw new RuntimeException("Пользователь с таким именем уже существует");
                        }
                        user.setUsername(request.getUsername());
                    }
                    
                    if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
                        if (repository.existsByEmail(request.getEmail())) {
                            throw new RuntimeException("Пользователь с таким email уже существует");
                        }
                        user.setEmail(request.getEmail());
                    }
                    
                    if (request.getPassword() != null) {
                        user.setPassword(passwordEncoder.encode(request.getPassword()));
                    }
                    
                    if (request.getFirstName() != null) {
                        user.setFirstName(request.getFirstName());
                    }
                    
                    if (request.getLastName() != null) {
                        user.setLastName(request.getLastName());
                    }
                    
                    if (request.getPhoneNumber() != null) {
                        user.setPhoneNumber(request.getPhoneNumber());
                    }
                    
                    if (request.getRole() != null) {
                        user.setRole(request.getRole());
                    }
                    
                    if (request.getIsActive() != null) {
                        user.setIsActive(request.getIsActive());
                    }
                    
                    User savedUser = repository.save(user);
                    return UserDto.fromUser(savedUser);
                });
    }
    
    @Override
    public boolean deleteUser(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean deactivateUser(Long id) {
        return repository.findById(id)
                .map(user -> {
                    user.setIsActive(false);
                    repository.save(user);
                    return true;
                })
                .orElse(false);
    }
    
    @Override
    public boolean activateUser(Long id) {
        return repository.findById(id)
                .map(user -> {
                    user.setIsActive(true);
                    repository.save(user);
                    return true;
                })
                .orElse(false);
    }
}

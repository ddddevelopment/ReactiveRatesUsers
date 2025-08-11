package com.reactiverates.users.application.service;

import com.reactiverates.users.api.model.CreateUserRequest;
import com.reactiverates.users.api.model.UpdateUserRequest;
import com.reactiverates.users.api.model.UserDto;
import com.reactiverates.users.domain.model.User;
import com.reactiverates.users.domain.service.UsersService;
import com.reactiverates.users.infrastructure.persistence.repository.UsersRepository;
import com.reactiverates.users.infrastructure.persistence.entity.UserEntity;

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
    
    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(UserEntity::toDomain)
                .map(UserDto::fromDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<UserDto> getUserById(Long id) {
        return repository.findById(id)
                .map(UserEntity::toDomain)
                .map(UserDto::fromDomain);
    }
    
    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        return repository.findByUsername(username)
                .map(UserEntity::toDomain)
                .map(UserDto::fromDomain);
    }
    
    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(UserEntity::toDomain)
                .map(UserDto::fromDomain);
    }
    
    @Override
    public List<UserDto> getUsersByRole(User.UserRole role) {
        return repository.findByRole(role).stream()
                .map(UserEntity::toDomain)
                .map(UserDto::fromDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserDto> getActiveUsers() {
        return repository.findByIsActive(true).stream()
                .map(UserEntity::toDomain)
                .map(UserDto::fromDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserDto> searchUsers(String searchTerm) {
        return repository.findBySearchTerm(searchTerm).stream()
                .map(UserEntity::toDomain)
                .map(UserDto::fromDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public UserDto createUser(CreateUserRequest request) {
        // Проверяем уникальность username и email
        if (repository.existsByUsername(request.username())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        
        if (repository.existsByEmail(request.email())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .role(request.role())
                .isActive(true)
                .build();
        
        UserEntity userEntity = UserEntity.fromDomain(user);
        UserEntity savedEntity = repository.save(userEntity);
        return UserDto.fromDomain(savedEntity.toDomain());
    }
    
    @Override
    public Optional<UserDto> updateUser(Long id, UpdateUserRequest request) {
        return repository.findById(id)
                .map(userEntity -> {
                    User user = userEntity.toDomain();
                    
                    if (request.username() != null && !request.username().equals(user.getUsername())) {
                        if (repository.existsByUsername(request.username())) {
                            throw new RuntimeException("Пользователь с таким именем уже существует");
                        }
                        user.setUsername(request.username());
                    }
                    
                    if (request.email() != null && !request.email().equals(user.getEmail())) {
                        if (repository.existsByEmail(request.email())) {
                            throw new RuntimeException("Пользователь с таким email уже существует");
                        }
                        user.setEmail(request.email());
                    }
                    
                    if (request.password() != null) {
                        user.setPassword(passwordEncoder.encode(request.password()));
                    }
                    
                    if (request.firstName() != null) {
                        user.setFirstName(request.firstName());
                    }
                    
                    if (request.lastName() != null) {
                        user.setLastName(request.lastName());
                    }
                    
                    if (request.phoneNumber() != null) {
                        user.setPhoneNumber(request.phoneNumber());
                    }
                    
                    if (request.role() != null) {
                        user.setRole(request.role());
                    }
                    
                    if (request.isActive() != null) {
                        user.setIsActive(request.isActive());
                    }
                    
                    UserEntity updatedEntity = UserEntity.fromDomain(user);
                    UserEntity savedEntity = repository.save(updatedEntity);
                    return UserDto.fromDomain(savedEntity.toDomain());
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
                .map(userEntity -> {
                    User user = userEntity.toDomain();
                    user.setIsActive(false);
                    UserEntity updatedEntity = UserEntity.fromDomain(user);
                    repository.save(updatedEntity);
                    return true;
                })
                .orElse(false);
    }
    
    @Override
    public boolean activateUser(Long id) {
        return repository.findById(id)
                .map(userEntity -> {
                    User user = userEntity.toDomain();
                    user.setIsActive(true);
                    UserEntity updatedEntity = UserEntity.fromDomain(user);
                    repository.save(updatedEntity);
                    return true;
                })
                .orElse(false);
    }
}

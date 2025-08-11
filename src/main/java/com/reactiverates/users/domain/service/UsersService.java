package com.reactiverates.users.domain.service;

import java.util.List;
import java.util.Optional;

import com.reactiverates.users.domain.model.CreateUserRequest;
import com.reactiverates.users.domain.model.UpdateUserRequest;
import com.reactiverates.users.domain.model.User;
import com.reactiverates.users.domain.model.UserDto;

public interface UsersService {
    List<UserDto> getAllUsers();
    Optional<UserDto> getUserById(Long id);
    Optional<UserDto> getUserByUsername(String username);
    Optional<UserDto> getUserByEmail(String email);
    List<UserDto> getUsersByRole(User.UserRole role);
    List<UserDto> getActiveUsers();
    List<UserDto> searchUsers(String searchTerm);
    UserDto createUser(CreateUserRequest request);
    Optional<UserDto> updateUser(Long id, UpdateUserRequest request);
    boolean deleteUser(Long id);
    boolean deactivateUser(Long id);
    boolean activateUser(Long id);
}

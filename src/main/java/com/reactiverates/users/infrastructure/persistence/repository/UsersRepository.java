package com.reactiverates.users.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reactiverates.users.infrastructure.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByUsername(String username);
    
    Optional<UserEntity> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<UserEntity> findByRole(com.reactiverates.users.domain.model.User.UserRole role);
    
    List<UserEntity> findByIsActive(Boolean isActive);
    
    @Query("SELECT u FROM UserEntity u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    List<UserEntity> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT u FROM UserEntity u WHERE u.username LIKE %:search% OR u.email LIKE %:search% OR u.firstName LIKE %:search% OR u.lastName LIKE %:search%")
    List<UserEntity> findBySearchTerm(@Param("search") String search);
}

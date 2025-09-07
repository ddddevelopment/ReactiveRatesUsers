package com.reactiverates.users.infrastructure.grpc;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

import com.reactiverates.users.domain.model.User;
import com.reactiverates.users.domain.model.UserDto;
import com.reactiverates.users.domain.service.UsersService;
import com.reactiverates.users.grpc.CreateUserRequest;
import com.reactiverates.users.grpc.GetUserByIdRequest;
import com.reactiverates.users.grpc.GetUserByUsernameRequest;
import com.reactiverates.users.grpc.UserResponse;
import com.reactiverates.users.grpc.UserRole;
import com.reactiverates.users.grpc.UsersServiceGrpc.UsersServiceImplBase;

import io.grpc.stub.StreamObserver;

@GrpcService
public class UsersGrpcService extends UsersServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(UsersGrpcService.class);

    private final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;
    private final UsersService usersService;

    public UsersGrpcService(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        logger.debug("gRPC createUser request received for username: {}", request.getUsername());

        try {
            var userRequest = new com.reactiverates.users.domain.model.CreateUserRequest(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getPhoneNumber(),
                    convertToDomainRole(request.getRole()));

            long startTime = System.currentTimeMillis();
            UserDto created = usersService.createUser(userRequest);
            long endTime = System.currentTimeMillis();

            logger.info("gRPC createUser completed successfully for username: {} in {}ms", request.getUsername(), (endTime - startTime));
            logger.debug("gRPC createUser response: userId={}, email={}", created.id(), created.email());

            responseObserver.onNext(toUserResponse(created));
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("gRPC createUser failed for username: {} - Error: {}", request.getUsername(), e.getMessage(), e);
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Failed to create user: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<UserResponse> responseObserver) {
        logger.debug("gRPC getUserById request received for userId: {}", request.getUserId());

        try {
            long startTime = System.currentTimeMillis();
            Optional<UserDto> user = usersService.getUserById(request.getUserId());
            long endTime = System.currentTimeMillis();

            if (user.isPresent()) {
                logger.info("gRPC getUserById found user with ID: {} in {}ms", request.getUserId(), (endTime - startTime));
                logger.debug("gRPC getUserById response: username={}, email={}", user.get().username(), user.get().email());
                responseObserver.onNext(toUserResponse(user.get()));
            } else {
                logger.warn("gRPC getUserById user not found with ID: {}", request.getUserId());
                responseObserver.onNext(toUserNotFoundResponse("User with ID " + request.getUserId() + " not found"));
            }
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("gRPC getUserById failed for userId: {} - Error: {}", request.getUserId(), e.getMessage(), e);
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Failed to get user: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void getUserByUsername(GetUserByUsernameRequest request, StreamObserver<UserResponse> responseObserver) {
        logger.debug("gRPC getUserByUsername request received for username: {}", request.getUsername());

        try {
            long startTime = System.currentTimeMillis();
            Optional<UserDto> user = usersService.getUserByUsername(request.getUsername());
            long endTime = System.currentTimeMillis();

            if (user.isPresent()) {
                logger.info("gRPC getUserByUsername found user with username: {} in {}ms", request.getUsername(), (endTime - startTime));
                logger.debug("gRPC getUserByUsername response: userId={}, email={}", user.get().id(), user.get().email());
                responseObserver.onNext(toUserResponse(user.get()));
            } else {
                logger.warn("gRPC getUserByUsername user not found with username: {}", request.getUsername());
                responseObserver.onNext(toUserNotFoundResponse("User with username '" + request.getUsername() + "' not found"));
            }
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("gRPC getUserByUsername failed for username: {} - Error: {}", request.getUsername(), e.getMessage(), e);
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Failed to get user: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    private User.UserRole convertToDomainRole(com.reactiverates.users.grpc.UserRole role) {
        return switch (role) {
            case ADMIN -> User.UserRole.ADMIN;
            case MODERATOR -> User.UserRole.MODERATOR;
            default -> User.UserRole.USER;
        };
    }

    private UserResponse toUserResponse(UserDto u) {
        return UserResponse.newBuilder()
                .setId(u.id())
                .setUsername(u.username())
                .setEmail(u.email() != null ? u.email() : "")
                .setFirstName(u.firstName() != null ? u.firstName() : "")
                .setLastName(u.lastName() != null ? u.lastName() : "")
                .setPhoneNumber(u.phoneNumber() != null ? u.phoneNumber() : "")
                .setRole(convertToGrpcRole(u.role()))
                .setIsActive(Boolean.TRUE.equals(u.isActive()))
                .setCreatedAt(u.createdAt() != null ? u.createdAt().format(ISO) : "")
                .setUpdatedAt(u.updatedAt() != null ? u.updatedAt().format(ISO) : "")
                .setFullName(buildFullName(u))
                .setPasswordHash(u.passwordHash() != null ? u.passwordHash() : "")
                .setFound(true)
                .setMessage("User found")
                .build();
    }

    private String buildFullName(UserDto u) {
        if (u.firstName() != null && u.lastName() != null)
            return u.firstName() + " " + u.lastName();
        if (u.firstName() != null)
            return u.firstName();
        if (u.lastName() != null)
            return u.lastName();
        return u.username();
    }

    private UserResponse toUserNotFoundResponse(String message) {
        return UserResponse.newBuilder()
                .setId(0)
                .setUsername("")
                .setEmail("")
                .setFirstName("")
                .setLastName("")
                .setPhoneNumber("")
                .setRole(UserRole.USER)
                .setIsActive(false)
                .setCreatedAt("")
                .setUpdatedAt("")
                .setFullName("")
                .setPasswordHash("")
                .setFound(false)
                .setMessage(message)
                .build();
    }

    private UserRole convertToGrpcRole(User.UserRole role) {
        return switch (role) {
            case ADMIN -> UserRole.ADMIN;
            case MODERATOR -> UserRole.MODERATOR;
            default -> UserRole.USER;
        };
    }
}

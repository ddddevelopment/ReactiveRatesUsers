package com.reactiverates.users.infrastructure.grpc;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

@GrpcService
public class UsersGrpcService extends UsersServiceImplBase {

    private final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;
    private final UsersService usersService;

    public UsersGrpcService(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        var userRequest = new com.reactiverates.users.domain.model.CreateUserRequest(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber(),
                convertToDomainRole(request.getRole()));

        UserDto created = usersService.createUser(userRequest);

        responseObserver.onNext(toUserResponse(created));
        responseObserver.onCompleted();
    }

    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<UserResponse> responseObserver) {
        Optional<UserDto> user = usersService.getUserById(request.getUserId());

        if (user.isPresent()) {
            responseObserver.onNext(toUserResponse(user.get()));
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.withDescription("User not found").asRuntimeException());
        }
    }

    @Override
    public void getUserByUsername(GetUserByUsernameRequest request, StreamObserver<UserResponse> responseObserver) {
        Optional<UserDto> user = usersService.getUserByUsername(request.getUsername());

        if (user.isPresent()) {
            responseObserver.onNext(toUserResponse(user.get()));
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.withDescription("User not found").asRuntimeException());
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

    private UserRole convertToGrpcRole(User.UserRole role) {
        return switch (role) {
            case ADMIN -> UserRole.ADMIN;
            case MODERATOR -> UserRole.MODERATOR;
            default -> UserRole.USER;
        };
    }
}

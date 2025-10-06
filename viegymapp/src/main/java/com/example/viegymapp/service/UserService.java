package com.example.viegymapp.service;

import com.example.viegymapp.dto.request.UserCreationRequest;
import com.example.viegymapp.dto.request.UserUpdateRequest;
import com.example.viegymapp.dto.response.UserResponse;
import com.example.viegymapp.entity.Enum.PredefinedRole;

import java.util.List;
import java.util.UUID;


public interface UserService {

    UserResponse createUser(UserCreationRequest request);
    UserResponse getCurrentUser();
    UserResponse updateCurrentUser(UserUpdateRequest request);
    UserResponse updateUserById(UUID id, UserUpdateRequest request);
    UserResponse getUserById(UUID id);
    List<UserResponse> getAllUsers();
    UserResponse assignRoleToUser(UUID userId, PredefinedRole roleName);
    void deleteUser(UUID id);



}

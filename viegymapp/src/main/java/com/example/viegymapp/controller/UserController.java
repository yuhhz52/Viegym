package com.example.viegymapp.controller;


import com.example.viegymapp.dto.request.UserCreationRequest;
import com.example.viegymapp.dto.request.UserUpdateRequest;
import com.example.viegymapp.dto.response.ApiResponse;
import com.example.viegymapp.dto.response.UserResponse;
import com.example.viegymapp.entity.Enum.PredefinedRole;
import com.example.viegymapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreationRequest request) {
       return ApiResponse.<UserResponse>builder()
               .result(userService.createUser(request))
               .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable UUID id){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(id))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<UserResponse>>getAllUsers(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getCurrentUser(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getCurrentUser())
                .build();
    }

    @PatchMapping("/my-info")
    public ApiResponse<UserResponse> updateCurrentUser(@Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateCurrentUser(request))
                .build();
    }


    @PatchMapping("/{id}")
    public ApiResponse<UserResponse> updateUserById(@PathVariable UUID id,
                                                    @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUserById(id, request))
                .build();
    }

    // Gán role cho user
    @PostMapping("/{id}/assign-role")
    public ApiResponse<UserResponse> assignRoleToUser(@PathVariable UUID id,
                                                      @RequestParam PredefinedRole roleName) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.assignRoleToUser(id, roleName))
                .build();
    }


    @DeleteMapping("/{id}")
    public ApiResponse<UUID> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ApiResponse.<UUID>builder()
                .result(id)
                .build();
    }

}

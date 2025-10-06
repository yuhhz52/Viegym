package com.example.viegymapp.controller;

import com.example.viegymapp.dto.request.LoginRequest;
import com.example.viegymapp.dto.response.ApiResponse;
import com.example.viegymapp.dto.response.MessageResponse;
import com.example.viegymapp.dto.response.TokenRefreshResponse;
import com.example.viegymapp.dto.response.UserInfoResponse;
import com.example.viegymapp.security.services.UserDetailsImpl;
import com.example.viegymapp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResponse<UserInfoResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                              HttpServletResponse response) {
        return ApiResponse.<UserInfoResponse>builder()
                .result(authService.login(loginRequest, response))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<MessageResponse> logout(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                  HttpServletResponse response) {
        return ApiResponse.<MessageResponse>builder()
                .result(authService.logout(userDetails.getId(), response))
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<TokenRefreshResponse> refreshToken(HttpServletRequest request,
                                             HttpServletResponse response) {
        return ApiResponse.<TokenRefreshResponse>builder()
                .result( authService.refreshToken(request, response))
                .build();

    }

}

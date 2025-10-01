package com.example.viegymapp.controller;

import com.example.viegymapp.dto.request.LoginRequest;
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
    public UserInfoResponse login(@Valid @RequestBody LoginRequest loginRequest,
                                  HttpServletResponse response) {
        return authService.login(loginRequest, response);
    }

    @PostMapping("/logout")
    public MessageResponse logout(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                  HttpServletResponse response) {
        return authService.logout(userDetails.getId(), response);
    }


    @PostMapping("/refresh")
    public TokenRefreshResponse refreshToken(HttpServletRequest request,
                                             HttpServletResponse response) {
        return authService.refreshToken(request, response);
    }

}

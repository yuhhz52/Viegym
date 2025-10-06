package com.example.viegymapp.service;

import com.example.viegymapp.dto.request.LoginRequest;
import com.example.viegymapp.dto.response.MessageResponse;
import com.example.viegymapp.dto.response.TokenRefreshResponse;
import com.example.viegymapp.dto.response.UserInfoResponse;
import com.example.viegymapp.entity.RefreshToken;
import com.example.viegymapp.exception.AppException;
import com.example.viegymapp.exception.ErrorCode;
import com.example.viegymapp.repository.RefreshTokenRepository;
import com.example.viegymapp.security.jwt.JwtUtils;
import com.example.viegymapp.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public UserInfoResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(), loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Tạo token và set vào cookie
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString());

        return new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet())
        );
    }

    public MessageResponse logout(UUID userId, HttpServletResponse response) {
        refreshTokenRepository.deleteByUserId(userId);

        ResponseCookie cleanJwt = ResponseCookie.from("viegym-jwt", "")
                .path("/api")
                .httpOnly(true)
                .maxAge(0)
                .build();

        ResponseCookie cleanRefresh = ResponseCookie.from("viegym-jwt-refresh", "")
                .path("/api/auth")
                .httpOnly(true)
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cleanJwt.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cleanRefresh.toString());

        return new MessageResponse("Logout successful");
    }



    public TokenRefreshResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if (refreshToken != null && !refreshToken.isEmpty()) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        String newAccessToken = jwtUtils.generateTokenFromUsername(user.getUserName());
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user.getUserName());
                        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

                        return TokenRefreshResponse.builder()
                                .accessToken(newAccessToken)
                                .refreshToken(refreshToken)
                                .build();
                    })
                    .orElseThrow(() -> new AppException(ErrorCode.TOKEN_REFRESH_FAILED));
        }

        throw new AppException(ErrorCode.TOKEN_REFRESH_FAILED);
    }

}

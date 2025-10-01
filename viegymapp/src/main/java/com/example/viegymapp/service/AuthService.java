package com.example.viegymapp.security.services;

import com.example.viegymapp.dto.request.LoginRequest;
import com.example.viegymapp.dto.response.MessageResponse;
import com.example.viegymapp.dto.response.TokenRefreshResponse;
import com.example.viegymapp.dto.response.UserInfoResponse;
import com.example.viegymapp.entity.RefreshToken;
import com.example.viegymapp.exception.TokenRefreshException;
import com.example.viegymapp.security.jwt.JwtUtils;
import com.example.viegymapp.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

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

    public MessageResponse logout(HttpServletResponse response) {
        ResponseCookie cleanJwt = jwtUtils.getCleanJwtCookie();
        ResponseCookie cleanRefresh = jwtUtils.getCleanJwtRefreshCookie();

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
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token không có trong cơ sở dữ liệu!"));
        }

        throw new TokenRefreshException(null, "Refresh Token đang trống!");
    }

}

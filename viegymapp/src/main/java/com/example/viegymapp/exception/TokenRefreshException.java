package com.example.viegymapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static final String EXPIRED_MESSAGE = "Refresh token has expired. Please log in again!";
    public static final String TOKEN_NOT_FOUND_MESSAGE = "Refresh token not in database!";
    public static final String TOKEN_EMPTY_MESSAGE = "Refresh Token is empty!";

    // Constructor mặc định với token + message constant
    public TokenRefreshException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token != null ? token : "N/A", message));
    }

    // Constructor chỉ dùng message, không cần token
    public TokenRefreshException(String message) {
        super(message);
    }
}

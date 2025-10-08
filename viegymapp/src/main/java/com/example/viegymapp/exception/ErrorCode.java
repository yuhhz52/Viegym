package com.example.viegymapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1009, "Default role does not exist", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_USED(1010, "Email already in use", HttpStatus.BAD_REQUEST),
    TOKEN_REFRESH_FAILED(1011, "Token refresh failed", HttpStatus.FORBIDDEN),
    EXERCISE_NOT_FOUND(1012, "Exercise not found", HttpStatus.NOT_FOUND),
    WORKOUT_PROGRAM_NOT_FOUND(1013, "Workout program not found", HttpStatus.NOT_FOUND),
    INVALID_FILE_TYPE(1014, "Invalid file type", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1015, "File size too large", HttpStatus.BAD_REQUEST),
    DUPLICATE_ENTRY(1016, "Duplicate entry", HttpStatus.CONFLICT),
    RESOURCE_NOT_FOUND(1017, "Resource not found", HttpStatus.NOT_FOUND),
    INVALID_REQUEST_PARAMETER(1018, "Invalid request parameter", HttpStatus.BAD_REQUEST),
    DATABASE_ERROR(1019, "Database operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_FAILED(1020, "Validation failed", HttpStatus.BAD_REQUEST),
    CONSTRAINT_VIOLATION(1021, "Constraint violation", HttpStatus.BAD_REQUEST),
    METHOD_NOT_SUPPORTED(1022, "Request method not supported", HttpStatus.METHOD_NOT_ALLOWED),
    MEDIA_NOT_FOUND(1023, "ExerciseMedia not found", HttpStatus.NOT_FOUND),
    PROGRAM_NOT_FOUND(1024, "Program not found",HttpStatus.NOT_FOUND),
    PROGRAM_EXERCISE_NOT_FOUND(1025, "ProgramExercise not found",HttpStatus.NOT_FOUND),
    SESSION_NOT_FOUND(1026, "Session not found",HttpStatus.NOT_FOUND),
    LOG_NOT_FOUND(1027, "Log not found",HttpStatus.NOT_FOUND),
    HEALTH_LOG_NOT_FOUND(1028, "Health log not found",HttpStatus.NOT_FOUND),

    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}

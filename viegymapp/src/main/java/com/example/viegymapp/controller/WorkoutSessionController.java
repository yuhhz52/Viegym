package com.example.viegymapp.controller;

import com.example.viegymapp.dto.request.SessionExerciseLogRequest;
import com.example.viegymapp.dto.request.WorkoutSessionRequest;
import com.example.viegymapp.dto.response.ApiResponse;
import com.example.viegymapp.dto.response.SessionExerciseLogResponse;
import com.example.viegymapp.dto.response.WorkoutSessionResponse;
import com.example.viegymapp.service.SessionExerciseLogService;
import com.example.viegymapp.service.WorkoutSessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutSessionController {
    @Autowired
    private WorkoutSessionService sessionService;
    @Autowired
    private SessionExerciseLogService logService;

    @PostMapping("/sessions")
    public ApiResponse<WorkoutSessionResponse> createSession(@RequestBody WorkoutSessionRequest req) {
        return ApiResponse.<WorkoutSessionResponse>builder()
                .result(sessionService.createSession(req))
                .build();
    }

    @GetMapping("/sessions")
    public ApiResponse<List<WorkoutSessionResponse>> getAllSession() {
        return ApiResponse.<List<WorkoutSessionResponse>>builder()
                .result(sessionService.getAllSession())
                .build();
    }

    @GetMapping("/sessions/{id}")
    public ApiResponse<WorkoutSessionResponse> getSessionById(@PathVariable UUID id) {
        return ApiResponse.<WorkoutSessionResponse>builder()
                .result(sessionService.getSessionById(id))
                .build();
    }

    @PutMapping("/sessions/{id}")
    public ApiResponse<WorkoutSessionResponse> updateSession(@PathVariable UUID id,
                                                             @RequestBody WorkoutSessionRequest req) {
        return ApiResponse.<WorkoutSessionResponse>builder()
                .result(sessionService.updateSession(id, req))
                .build();
    }

    @DeleteMapping("/sessions/{id}")
    public ApiResponse<UUID> delete(@PathVariable UUID id) {
        sessionService.deleteSession(id);
        return ApiResponse.<UUID>builder()
                .result(id)
                .build();
    }

    @GetMapping("/sessions/{sessionId}/logs")
    public ApiResponse<List<SessionExerciseLogResponse>> getLogsBySession(
            @PathVariable UUID sessionId) {
        return ApiResponse.<List<SessionExerciseLogResponse>>builder()
                .result(logService.getLogBySessionId(sessionId))
                .build();
    }

    @PostMapping("/sessions/{sessionId}/logs")
    public ApiResponse<SessionExerciseLogResponse> createLog(@PathVariable UUID sessionId,
                                                             @Valid @RequestBody SessionExerciseLogRequest request) {
        request.setSessionId(sessionId);
        return ApiResponse.<SessionExerciseLogResponse>builder()
                .result(logService.createLog(request))
                .build();
    }

    @PutMapping("/logs/{id}")
    public ApiResponse<SessionExerciseLogResponse> updateLog(
            @PathVariable UUID id,
            @Valid @RequestBody SessionExerciseLogRequest request) {
        return ApiResponse.<SessionExerciseLogResponse>builder()
                .result(logService.updateLog(id, request))
                .build();
    }

    @DeleteMapping("/logs/{id}")
    public ApiResponse<UUID> deleteLog(@PathVariable UUID id) {
        logService.deleteLog(id);
        return ApiResponse.<UUID>builder()
                .result(id)
                .build();
    }
}

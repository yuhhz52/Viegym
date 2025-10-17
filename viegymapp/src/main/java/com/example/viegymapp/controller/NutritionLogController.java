package com.example.viegymapp.controller;

import com.example.viegymapp.dto.request.NutritionLogRequest;
import com.example.viegymapp.dto.response.ApiResponse;
import com.example.viegymapp.dto.response.NutritionLogResponse;
import com.example.viegymapp.service.NutritionLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nutrition")
@RequiredArgsConstructor
public class NutritionLogController {

    private final NutritionLogService nutritionLogService;

    /**
     * GET /nutrition?userId={userId}&date={yyyy-MM-dd}
     */
    @GetMapping
    public ApiResponse<List<NutritionLogResponse>> getLogs(
            @RequestParam UUID userId,
            @RequestParam(required = false) LocalDate date) {
        return ApiResponse.<List<NutritionLogResponse>>builder()
                .result(nutritionLogService.getLogs(userId, date))
                .build();
    }

    /**
     * POST /nutrition
     */
    @PostMapping
    public ApiResponse<NutritionLogResponse> createLog(
            @Valid @RequestBody NutritionLogRequest request) {
        return ApiResponse.<NutritionLogResponse>builder()
                .result(nutritionLogService.createLog(request))
                .build();
    }

    /**
     * PUT /nutrition/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<NutritionLogResponse> updateLog(
            @PathVariable UUID id,
            @Valid @RequestBody NutritionLogRequest request) {
        return ApiResponse.<NutritionLogResponse>builder()
                .result(nutritionLogService.updateLog(id, request))
                .build();
    }

    /**
     * DELETE /nutrition/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<UUID> deleteLog(@PathVariable UUID id) {
        nutritionLogService.deleteLog(id);
        return ApiResponse.<UUID>builder()
                .result(id)
                .build();
    }
}

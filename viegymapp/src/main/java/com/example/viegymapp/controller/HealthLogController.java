// controller/HealthLogController.java
package com.example.viegymapp.controller;

import com.example.viegymapp.dto.request.HealthLogRequest;
import com.example.viegymapp.dto.response.ApiResponse;
import com.example.viegymapp.dto.response.HealthLogResponse;
import com.example.viegymapp.service.HealthLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/health")
public class HealthLogController {

    @Autowired
    private HealthLogService healthLogService;

    @GetMapping
    public ApiResponse<List<HealthLogResponse>> getHealthLogs() {
        return ApiResponse.<List<HealthLogResponse>>builder()
                .result(healthLogService.getHealthLogsOfCurrentUser())
                .build();
    }

    @PostMapping
    public ApiResponse<HealthLogResponse> create(@RequestBody HealthLogRequest request) {
        return ApiResponse.<HealthLogResponse>builder()
                .result(healthLogService.createHealthLog(request))
                .build();

    }

    @PutMapping("/{id}")
    public ApiResponse<HealthLogResponse> update(@PathVariable UUID id, @RequestBody HealthLogRequest request) {
        return ApiResponse.<HealthLogResponse>builder()
                .result(healthLogService.updateHealthLog(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<UUID> delete(@PathVariable UUID id) {
        return ApiResponse.<UUID>builder()
                .result(id)
                .build();
    }
}

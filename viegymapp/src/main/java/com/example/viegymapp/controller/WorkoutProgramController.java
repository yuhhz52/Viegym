package com.example.viegymapp.controller;

import com.example.viegymapp.dto.request.*;
import com.example.viegymapp.dto.response.*;
import com.example.viegymapp.service.WorkoutProgramService;
import com.example.viegymapp.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/programs")
@RequiredArgsConstructor
public class WorkoutProgramController {

    private final WorkoutProgramService programService;

    @GetMapping
    public ApiResponse<List<WorkoutProgramResponse>> getAllPrograms() {
        return ApiResponse.<List<WorkoutProgramResponse>>builder()
                .result(programService.getAllPrograms())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<WorkoutProgramResponse> getProgram(@PathVariable UUID id) {
        return ApiResponse.<WorkoutProgramResponse>builder()
                .result(programService.getProgramById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<WorkoutProgramResponse> createProgram(@RequestBody WorkoutProgramRequest request) {
        return ApiResponse.<WorkoutProgramResponse>builder()
                .result(programService.createProgram(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<WorkoutProgramResponse> updateProgram(@PathVariable UUID id,
                                                             @RequestBody WorkoutProgramRequest request) {
        return ApiResponse.<WorkoutProgramResponse>builder()
                .result(programService.updateProgram(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<UUID> deleteProgram(@PathVariable UUID id) {
        programService.deleteProgram(id);
        return ApiResponse.<UUID>builder()
                .result(id)
                .build();
    }

    @GetMapping("/{id}/exercises")
    public ApiResponse<List<ProgramExerciseResponse>> getExercises(@PathVariable UUID id) {
        return ApiResponse.<List<ProgramExerciseResponse>>builder()
                .result(programService.getExercisesInProgram(id))
                .build();
    }

    @PostMapping("/{id}/exercises")
    public ApiResponse<ProgramExerciseResponse> addExercise(@PathVariable UUID id,
                                                            @RequestBody ProgramExerciseRequest request) {
        return ApiResponse.<ProgramExerciseResponse>builder()
                .result(programService.addExerciseToProgram(id, request))
                .build();
    }

    @PutMapping("/exercises/{id}")
    public ApiResponse<ProgramExerciseResponse> updateExercise(@PathVariable UUID id,
                                                               @RequestBody ProgramExerciseRequest request) {
        return ApiResponse.<ProgramExerciseResponse>builder()
                .result(programService.updateProgramExercise(id, request))
                .build();
    }

    @DeleteMapping("/exercises/{id}")
    public ApiResponse<UUID> deleteExercise(@PathVariable UUID id) {
        programService.deleteProgramExercise(id);
        return ApiResponse.<UUID>builder()
                .result(id)
                .build();
    }
}

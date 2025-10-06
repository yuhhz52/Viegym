package com.example.viegymapp.controller;

import com.example.viegymapp.dto.request.ExerciseMediaRequest;
import com.example.viegymapp.dto.request.ExerciseRequest;
import com.example.viegymapp.dto.response.ApiResponse;
import com.example.viegymapp.dto.response.ExerciseMediaResponse;
import com.example.viegymapp.dto.response.ExerciseResponse;
import com.example.viegymapp.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping
    public ApiResponse<List<ExerciseResponse>> getExercises(@RequestParam(required = false) String difficulty,
                                                            @RequestParam(required = false) String muscleGroup){
        return ApiResponse.<List<ExerciseResponse>>builder()
                .result(exerciseService.getExercises(difficulty,muscleGroup))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ExerciseResponse> getExerciseById(@PathVariable UUID id) {
        return ApiResponse.<ExerciseResponse>builder()
                .result(exerciseService.getExerciseById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<ExerciseResponse> createExercise(@RequestBody ExerciseRequest createRequest) {
        return ApiResponse.<ExerciseResponse>builder()
                .result(exerciseService.createExercise(createRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ExerciseResponse> updateExercise(@PathVariable UUID id,
                                                        @RequestBody ExerciseRequest updateRequest) {
        return ApiResponse.<ExerciseResponse>builder()
                .result(exerciseService.updateExercise(id, updateRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<UUID> deleteExercise(@PathVariable UUID id) {
        exerciseService.deleteExercise(id);
        return ApiResponse.<UUID>builder()
                .result(id)
                .build();
    }

    @GetMapping("/{exerciseId}/media")
    public ApiResponse<List<ExerciseMediaResponse>> getMedia(@PathVariable UUID exerciseId) {
        return ApiResponse.<List<ExerciseMediaResponse>>builder()
                .result(exerciseService.getMedia(exerciseId))
                .build();
    }

    @PostMapping("/{exerciseId}/media")
    public ApiResponse<ExerciseMediaResponse> addMedia(@PathVariable UUID exerciseId,
                                                       @RequestBody ExerciseMediaRequest mediaCreateRequest) {
        return ApiResponse.<ExerciseMediaResponse>builder()
                .result(exerciseService.addMedia(exerciseId, mediaCreateRequest))
                .build();
    }

    @DeleteMapping("/media/{mediaId}")
    public ApiResponse<UUID> deleteMedia(@PathVariable UUID mediaId) {
        exerciseService.deleteMedia(mediaId);
        return ApiResponse.<UUID>builder()
                .result(mediaId)
                .build();
    }






}

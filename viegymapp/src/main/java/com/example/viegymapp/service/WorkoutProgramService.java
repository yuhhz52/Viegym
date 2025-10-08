package com.example.viegymapp.service;

import com.example.viegymapp.dto.request.WorkoutProgramRequest;
import com.example.viegymapp.dto.request.ProgramExerciseRequest;
import com.example.viegymapp.dto.response.WorkoutProgramResponse;
import com.example.viegymapp.dto.response.ProgramExerciseResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface WorkoutProgramService {
    List<WorkoutProgramResponse> getAllPrograms();

    WorkoutProgramResponse getProgramById(UUID id);

    WorkoutProgramResponse createProgram(WorkoutProgramRequest request);

    WorkoutProgramResponse updateProgram(UUID id, WorkoutProgramRequest request);

    void deleteProgram(UUID id);

    List<ProgramExerciseResponse> getExercisesInProgram(UUID programId);

    ProgramExerciseResponse addExerciseToProgram(UUID programId, ProgramExerciseRequest request);

    ProgramExerciseResponse updateProgramExercise(UUID programExerciseId, ProgramExerciseRequest request);

    void deleteProgramExercise(UUID programExerciseId);
}

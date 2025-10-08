package com.example.viegymapp.service.Impl;

import com.example.viegymapp.dto.request.ProgramExerciseRequest;
import com.example.viegymapp.dto.request.WorkoutProgramRequest;
import com.example.viegymapp.dto.response.ProgramExerciseResponse;
import com.example.viegymapp.dto.response.WorkoutProgramResponse;
import com.example.viegymapp.entity.Exercise;
import com.example.viegymapp.entity.ProgramExercise;
import com.example.viegymapp.entity.WorkoutProgram;
import com.example.viegymapp.exception.AppException;
import com.example.viegymapp.exception.ErrorCode;
import com.example.viegymapp.mapper.ProgramExerciseMapper;
import com.example.viegymapp.mapper.WorkoutProgramMapper;
import com.example.viegymapp.repository.ExerciseRepository;
import com.example.viegymapp.repository.ProgramExerciseRepository;
import com.example.viegymapp.repository.WorkoutProgramRepository;
import com.example.viegymapp.service.WorkoutProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkoutProgramServiceImpl implements WorkoutProgramService {

    @Autowired
    private WorkoutProgramRepository programRepo;
    @Autowired
    private ProgramExerciseRepository programExerciseRepo;
    @Autowired
    private ExerciseRepository exerciseRepo;
    @Autowired
    private WorkoutProgramMapper programMapper;
    @Autowired
    private ProgramExerciseMapper programExerciseMapper;

    @Override
    public List<WorkoutProgramResponse> getAllPrograms() {
        return programRepo.findAll().stream()
                .map(programMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WorkoutProgramResponse getProgramById(UUID id) {
        return programRepo.findById(id)
                .map(programMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.PROGRAM_NOT_FOUND));
    }

    @Override
    public WorkoutProgramResponse createProgram(WorkoutProgramRequest request) {
        WorkoutProgram program = WorkoutProgram.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .goal(request.getGoal())
                .durationWeeks(request.getDurationWeeks())
                .visibility(request.getVisibility())
                .build();
        return programMapper.toResponse(programRepo.save(program));
    }

    @Override
    public WorkoutProgramResponse updateProgram(UUID id, WorkoutProgramRequest request) {
        WorkoutProgram existing = programRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROGRAM_NOT_FOUND));

        WorkoutProgram updated = existing.toBuilder()
                .title(request.getTitle())
                .description(request.getDescription())
                .goal(request.getGoal())
                .durationWeeks(request.getDurationWeeks())
                .visibility(request.getVisibility())
                .build();

        return programMapper.toResponse(programRepo.save(updated));
    }


    @Override
    public void deleteProgram(UUID id) {
        if (!programRepo.existsById(id)) {
            throw new AppException(ErrorCode.PROGRAM_NOT_FOUND);
        }
        programRepo.deleteById(id);
    }

    @Override
    public List<ProgramExerciseResponse> getExercisesInProgram(UUID programId) {
        return programExerciseRepo.findByProgramId(programId).stream()
                .map(programExerciseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProgramExerciseResponse addExerciseToProgram(UUID programId, ProgramExerciseRequest request) {
        WorkoutProgram program = programRepo.findById(programId)
                .orElseThrow(() -> new AppException(ErrorCode.PROGRAM_NOT_FOUND));
        Exercise exercise = exerciseRepo.findById(UUID.fromString(request.getExerciseId()))
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));

        ProgramExercise pe = ProgramExercise.builder()
                .program(program)
                .exercise(exercise)
                .dayOfProgram(request.getDayOfProgram())
                .orderNo(request.getOrderNo())
                .sets(request.getSets())
                .reps(request.getReps())
                .weightScheme(request.getWeightScheme())
                .restSeconds(request.getRestSeconds())
                .notes(request.getNotes())
                .build();

        return programExerciseMapper.toResponse(programExerciseRepo.save(pe));
    }

    @Override
    public ProgramExerciseResponse updateProgramExercise(UUID programExerciseId, ProgramExerciseRequest request) {
        ProgramExercise existing = programExerciseRepo.findById(programExerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.PROGRAM_EXERCISE_NOT_FOUND));

        ProgramExercise updated = existing.toBuilder()
                .dayOfProgram(request.getDayOfProgram())
                .orderNo(request.getOrderNo())
                .sets(request.getSets())
                .reps(request.getReps())
                .weightScheme(request.getWeightScheme())
                .restSeconds(request.getRestSeconds())
                .notes(request.getNotes())
                .build();

        return programExerciseMapper.toResponse(programExerciseRepo.save(updated));
    }

    @Override
    public void deleteProgramExercise(UUID programExerciseId) {
        if (!programExerciseRepo.existsById(programExerciseId)) {
            throw new AppException(ErrorCode.PROGRAM_EXERCISE_NOT_FOUND);
        }
        programExerciseRepo.deleteById(programExerciseId);
    }
}

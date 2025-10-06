package com.example.viegymapp.service.Impl;

import com.example.viegymapp.dto.request.ExerciseMediaRequest;
import com.example.viegymapp.dto.request.ExerciseRequest;
import com.example.viegymapp.dto.response.ExerciseMediaResponse;
import com.example.viegymapp.dto.response.ExerciseResponse;
import com.example.viegymapp.entity.Enum.DifficultyLevel;
import com.example.viegymapp.entity.Exercise;
import com.example.viegymapp.entity.ExerciseMedia;
import com.example.viegymapp.entity.Tag;
import com.example.viegymapp.entity.User;
import com.example.viegymapp.exception.AppException;
import com.example.viegymapp.exception.ErrorCode;
import com.example.viegymapp.mapper.ExerciseMapper;
import com.example.viegymapp.mapper.ExerciseMediaMapper;
import com.example.viegymapp.repository.ExerciseMediaRepository;
import com.example.viegymapp.repository.ExerciseRepository;
import com.example.viegymapp.repository.TagRepository;
import com.example.viegymapp.service.ExerciseService;
import com.example.viegymapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private ExerciseMediaRepository exerciseMediaRepository;
    @Autowired
    private ExerciseMapper exerciseMapper;
    @Autowired
    private ExerciseMediaMapper exerciseMediaMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<ExerciseResponse> getExercises(String difficulty, String muscleGroup) {
        List<Exercise> exercises;

        if (difficulty != null && !difficulty.isBlank() &&
                muscleGroup != null && !muscleGroup.isBlank()) {
            DifficultyLevel level = parseDifficulty(difficulty);
            exercises = exerciseRepository.findByDifficultyAndMuscleGroup(level, muscleGroup);

        } else if (difficulty != null && !difficulty.isBlank()) {
            DifficultyLevel level = parseDifficulty(difficulty);
            exercises = exerciseRepository.findByDifficulty(level);

        } else if (muscleGroup != null && !muscleGroup.isBlank()) {
            exercises = exerciseRepository.findByMuscleGroup(muscleGroup);

        } else {
            exercises = exerciseRepository.findAll();
        }

        if (exercises.isEmpty()) {
            throw new AppException(ErrorCode.EXERCISE_NOT_FOUND);
        }

        return exercises.stream()
                .map(exerciseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private DifficultyLevel parseDifficulty(String difficulty) {
        try {
            return DifficultyLevel.valueOf(difficulty.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_REQUEST_PARAMETER);
        }
    }



    @Override
    public ExerciseResponse getExerciseById(UUID id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
        return exerciseMapper.toResponseDTO(exercise);
    }

    @Override
    public ExerciseResponse createExercise(ExerciseRequest createRequest) {
        if (createRequest == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST_PARAMETER);
        }

        Exercise exercise = exerciseMapper.toEntity(createRequest);

        // Xử lý createdBy mặc định = currentUser
        if (exercise.getCreatedBy() == null || exercise.getCreatedBy().getId() == null) {
            var currentUser = userService.getCurrentUser();
            if (currentUser != null && currentUser.getId() != null) {
                User userRef = new User();
                userRef.setId(currentUser.getId());
                exercise.setCreatedBy(userRef);
            }
        }

        // Xử lý tags
        if (createRequest.getTags() != null && !createRequest.getTags().isEmpty()) {
            Set<Tag> tagEntities = createRequest.getTags().stream()
                    .map(tagName -> tagRepository.findByNameIgnoreCase(tagName)
                            .orElseGet(() -> {
                                Tag newTag = new Tag();
                                newTag.setName(tagName.trim());
                                return tagRepository.save(newTag);
                            })
                    ).collect(Collectors.toSet());

            exercise.setTags(tagEntities);
        }

        Exercise saved = exerciseRepository.save(exercise);

        return exerciseMapper.toResponseDTO(saved);
    }


    @Override
    public ExerciseResponse updateExercise(UUID id, ExerciseRequest updateRequest) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));

        if (updateRequest == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST_PARAMETER);
        }

        exerciseMapper.updateExercise(exercise, updateRequest);
        
        Exercise updated = exerciseRepository.save(exercise);
        return exerciseMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteExercise(UUID id) {
        if (!exerciseRepository.existsById(id)) {
            throw new AppException(ErrorCode.EXERCISE_NOT_FOUND);
        }
        exerciseRepository.deleteById(id);
    }

    @Override
    public List<ExerciseMediaResponse> getMedia(UUID exerciseId) {
        if (!exerciseRepository.existsById(exerciseId)) {
            throw new AppException(ErrorCode.EXERCISE_NOT_FOUND);
        }

        List<ExerciseMedia> mediaList = exerciseMediaRepository.findByExerciseId(exerciseId);

        if (mediaList.isEmpty()) {
            throw new AppException(ErrorCode.MEDIA_NOT_FOUND);
        }

        // Map sang DTO bằng MapStruct
        return mediaList.stream()
                .map(exerciseMediaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }



    @Override
    public ExerciseMediaResponse addMedia(UUID exerciseId, ExerciseMediaRequest mediaCreateRequest) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));

        if (mediaCreateRequest == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST_PARAMETER);
        }

        ExerciseMedia media = exerciseMediaMapper.toEntity(mediaCreateRequest);
        media.setId(null);
        media.setExercise(exercise);

        ExerciseMedia saved = exerciseMediaRepository.save(media);
        return exerciseMediaMapper.toResponseDTO(saved);
    }

    @Override
    public void deleteMedia(UUID mediaId) {
        ExerciseMedia media = exerciseMediaRepository.findById(mediaId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        exerciseMediaRepository.delete(media);
    }
}

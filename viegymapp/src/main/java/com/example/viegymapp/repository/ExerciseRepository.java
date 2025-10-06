package com.example.viegymapp.repository;

import com.example.viegymapp.entity.Enum.DifficultyLevel;
import com.example.viegymapp.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    List<Exercise> findByDifficulty(DifficultyLevel difficulty);
    List<Exercise> findByMuscleGroup(String muscleGroup);
    List<Exercise> findByDifficultyAndMuscleGroup(DifficultyLevel difficulty, String muscleGroup);
}

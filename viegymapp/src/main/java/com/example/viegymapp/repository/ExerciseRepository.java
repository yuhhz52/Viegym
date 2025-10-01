package com.example.viegymapp.repository;

import com.example.viegymapp.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    List<Exercise> findByDifficulty(String difficulty);
    List<Exercise> findByMuscleGroup(String muscleGroup);
}

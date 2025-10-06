package com.example.viegymapp.repository;

import com.example.viegymapp.entity.WorkoutProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface WorkoutProgramRepository extends JpaRepository<WorkoutProgram, UUID> {
}

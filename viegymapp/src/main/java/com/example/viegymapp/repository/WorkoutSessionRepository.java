package com.example.viegymapp.repository;

import com.example.viegymapp.entity.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, UUID> {
    List<WorkoutSession> findByUserId(UUID userId);
}

package com.example.viegymapp.repository;

import com.example.viegymapp.entity.SessionExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SessionExerciseLogRepository extends JpaRepository<SessionExerciseLog, UUID> {
    List<SessionExerciseLog> findBySessionId(UUID sessionId);
}

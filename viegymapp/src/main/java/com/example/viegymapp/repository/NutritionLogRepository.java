package com.example.viegymapp.repository;

import com.example.viegymapp.entity.NutritionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface NutritionLogRepository extends JpaRepository<NutritionLog, UUID> {

    List<NutritionLog> findByUserId(UUID userId);

    List<NutritionLog> findByUserIdAndLogDate(UUID userId, LocalDate logDate);
}

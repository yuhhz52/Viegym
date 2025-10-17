package com.example.viegymapp.repository;

import com.example.viegymapp.entity.HealthLog;
import com.example.viegymapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HealthLogRepository extends JpaRepository<HealthLog, UUID> {
    List<HealthLog> findByUser(User user);

}

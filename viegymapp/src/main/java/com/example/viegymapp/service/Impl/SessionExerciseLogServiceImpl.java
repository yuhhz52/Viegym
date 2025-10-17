package com.example.viegymapp.service.Impl;

import com.example.viegymapp.dto.request.SessionExerciseLogRequest;
import com.example.viegymapp.dto.response.SessionExerciseLogResponse;
import com.example.viegymapp.entity.SessionExerciseLog;
import com.example.viegymapp.exception.AppException;
import com.example.viegymapp.exception.ErrorCode;
import com.example.viegymapp.mapper.SessionExerciseLogMapper;
import com.example.viegymapp.repository.ExerciseRepository;
import com.example.viegymapp.repository.SessionExerciseLogRepository;
import com.example.viegymapp.repository.WorkoutSessionRepository;
import com.example.viegymapp.service.SessionExerciseLogService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SessionExerciseLogServiceImpl implements SessionExerciseLogService {
    @Autowired
    private SessionExerciseLogRepository logRepo;
    @Autowired
    private WorkoutSessionRepository sessionRepo;
    @Autowired
    private ExerciseRepository exerciseRepo;
    @Autowired
    private SessionExerciseLogMapper sessionExerciseLogMapper;

    @Override
    public SessionExerciseLogResponse createLog(SessionExerciseLogRequest request) {
        SessionExerciseLog sessionLog = sessionExerciseLogMapper.toEntity(request);
        
        sessionLog.setSession(sessionRepo.findById(request.getSessionId())
                .orElseThrow(() -> new AppException(ErrorCode.SESSION_NOT_FOUND)));
        
        sessionLog.setExercise(exerciseRepo.findById(request.getExerciseId())
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND)));
        
        SessionExerciseLog savedLog = logRepo.save(sessionLog);
        
        return sessionExerciseLogMapper.toResponse(savedLog);
    }

    @Override
    public List<SessionExerciseLogResponse> getLogBySessionId(UUID sessionId) {
        return logRepo.findBySessionId(sessionId)
                .stream().map(sessionExerciseLogMapper::toResponse).toList();
    }

    @Override
    public SessionExerciseLogResponse updateLog(UUID id, SessionExerciseLogRequest request) {
        SessionExerciseLog log = logRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LOG_NOT_FOUND));

        log.setSetNumber(request.getSetNumber());
        log.setRepsDone(request.getRepsDone());
        log.setWeightUsed(request.getWeightUsed());

        return sessionExerciseLogMapper.toResponse(logRepo.save(log));
    }

    @Override
    public void deleteLog(UUID id) {
        if (!logRepo.existsById(id)) {
            throw new AppException(ErrorCode.LOG_NOT_FOUND);
        }
        logRepo.deleteById(id);
    }
}

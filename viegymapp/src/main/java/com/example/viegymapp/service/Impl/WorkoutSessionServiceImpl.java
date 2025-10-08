package com.example.viegymapp.service.Impl;

import com.example.viegymapp.dto.request.WorkoutSessionRequest;
import com.example.viegymapp.dto.response.WorkoutSessionResponse;
import com.example.viegymapp.entity.User;
import com.example.viegymapp.entity.WorkoutSession;
import com.example.viegymapp.exception.AppException;
import com.example.viegymapp.exception.ErrorCode;
import com.example.viegymapp.mapper.WorkoutSessionMapper;
import com.example.viegymapp.repository.UserRepository;
import com.example.viegymapp.repository.WorkoutProgramRepository;
import com.example.viegymapp.repository.WorkoutSessionRepository;
import com.example.viegymapp.service.WorkoutSessionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WorkoutSessionServiceImpl implements WorkoutSessionService {
    @Autowired
    private WorkoutSessionRepository sessionRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkoutProgramRepository programRepo;
    @Autowired
    private WorkoutSessionMapper workoutSessionMapper;

    @Override
    public WorkoutSessionResponse createSession(WorkoutSessionRequest request) {
        var user = getCurrentUser();

        WorkoutSession session = workoutSessionMapper.toEntity(request);
        session.setUser(user);

        if (request.getProgramId() != null) {
            session.setProgram(programRepo.findById(request.getProgramId())
                    .orElseThrow(() -> new AppException(ErrorCode.PROGRAM_NOT_FOUND)));
        }
        return workoutSessionMapper.toResponse(sessionRepo.save(session));
    }

    @Override
    public List<WorkoutSessionResponse> getAllSession() {
        return sessionRepo.findAll().stream()
                .map(workoutSessionMapper::toResponse)
                .toList();
    }

    @Override
    public WorkoutSessionResponse getSessionById(UUID id) {
        return sessionRepo.findById(id)
                .map(workoutSessionMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.SESSION_NOT_FOUND));
    }

    @Override
    public WorkoutSessionResponse updateSession(UUID id, WorkoutSessionRequest request) {
        WorkoutSession session = sessionRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SESSION_NOT_FOUND));

        if (request.getSessionDate() != null) session.setSessionDate(request.getSessionDate());
        if (request.getDurationMinutes() != null) session.setDurationMinutes(request.getDurationMinutes());
        if (request.getNotes() != null) session.setNotes(request.getNotes());

        return workoutSessionMapper.toResponse(sessionRepo.save(session));
    }

    @Override
    public void deleteSession(UUID id) {
        if (!sessionRepo.existsById(id)) {
            throw new AppException(ErrorCode.SESSION_NOT_FOUND);
        }
        sessionRepo.deleteById(id);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}

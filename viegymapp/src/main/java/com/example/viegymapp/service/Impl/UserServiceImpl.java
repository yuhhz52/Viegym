package com.example.viegymapp.service.Impl;

import com.example.viegymapp.dto.request.UserCreationRequest;
import com.example.viegymapp.dto.request.UserUpdateRequest;
import com.example.viegymapp.dto.response.UserResponse;
import com.example.viegymapp.entity.Enum.PredefinedRole;
import com.example.viegymapp.entity.Role;
import com.example.viegymapp.entity.User;
import com.example.viegymapp.entity.UserRole;
import com.example.viegymapp.exception.AppException;
import com.example.viegymapp.exception.ErrorCode;
import com.example.viegymapp.mapper.UserMapper;
import com.example.viegymapp.repository.RoleRepository;
import com.example.viegymapp.repository.UserRepository;
import com.example.viegymapp.repository.UserRoleRepository;
import com.example.viegymapp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserMapper userMapper;


    @Override
    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);

        Role defaultRole = roleRepository.findByName(PredefinedRole.ROLE_USER)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        UserRole userRole = UserRole.builder()
                .user(user)
                .role(defaultRole)
                .assignedBy(user)
                .build();

        defaultRole.getUserRoles().add(userRole);
        user.getUserRoles().add(userRole);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }


    // Lấy thông tin người dùng hiện tại
    @Override
    public UserResponse getCurrentUser() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepository.findByUserName(username)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }


    // Cập nhật người dùng hiện tại
    @Transactional
    @Override
    public UserResponse updateCurrentUser(UserUpdateRequest request) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        // Nếu muốn đổi mật khẩu
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_ALREADY_USED);
            }
            user.setEmail(request.getEmail());
        }
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    // Cập nhật người dùng theo id dành cho admin
    @Transactional
    @Override
    public UserResponse updateUserById(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Cập nhật từ request
        userMapper.updateUser(user, request);

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_ALREADY_USED);
            }
            user.setEmail(request.getEmail());
        }

        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }


    // Hiện thị người dùng theo id
    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new  AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
    // Hiện thị tất cả người dùng
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    // Gán role
    @Override
    public UserResponse assignRoleToUser(UUID userId, PredefinedRole roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Lấy người dùng hiện tại (người thực hiện gán role)
        var context = SecurityContextHolder.getContext();
        String currentUsername = context.getAuthentication().getName();
        User currentUser = userRepository.findByUserName(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra user đã có role này chưa
        boolean exists = user.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getName().equals(roleName));

        if (!exists) {
            UserRole userRole = UserRole.builder()
                    .user(user)
                    .role(role)
                    .assignedBy(currentUser)
                    .build();

            user.getUserRoles().add(userRole);
            userRoleRepository.save(userRole);
        }
        return userMapper.toUserResponse(user);
    }

        //Xoa người dùng
    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.getUserRoles().clear();
        user.getRefreshTokens().clear();
        userRepository.delete(user);
    }


}

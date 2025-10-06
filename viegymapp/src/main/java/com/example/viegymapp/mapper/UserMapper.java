package com.example.viegymapp.mapper;

import com.example.viegymapp.dto.request.UserCreationRequest;
import com.example.viegymapp.dto.request.UserUpdateRequest;
import com.example.viegymapp.dto.response.UserResponse;
import com.example.viegymapp.entity.User;
import com.example.viegymapp.entity.UserRole;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    // Map từ request tạo user -> entity
    @Mapping(target = "id", ignore = true) // DB tự sinh
    @Mapping(target = "userRoles", expression = "java(new java.util.HashSet<>())")
    User toUser(UserCreationRequest request);

    // Map entity -> response
    @Mapping(target = "roles", source = "userRoles")
    UserResponse toUserResponse(User user);

    // Map update request -> entity
    @Mapping(target = "id", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    default Set<String> mapRoles(Set<UserRole> userRoles) {
        if (userRoles == null) return Collections.emptySet();
        return userRoles.stream()
                .map(ur -> ur.getRole().getName().name())
                .collect(Collectors.toSet());
    }
}

package com.example.viegymapp.mapper;

import com.example.viegymapp.dto.request.UserCreationRequest;
import com.example.viegymapp.dto.request.UserUpdateRequest;
import com.example.viegymapp.dto.response.UserResponse;
import com.example.viegymapp.entity.User;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", expression = "java(user.getUserRoles().stream()" +
            ".map(ur -> ur.getRole().getName().name())" +
            ".collect(java.util.stream.Collectors.toSet()))")
    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) //bất kỳ field nào trong request = null thì MapStruct sẽ bỏ qua
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}

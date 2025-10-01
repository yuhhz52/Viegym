package com.example.viegymapp.dto.response;

import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Lấy thông tin trả cho cookie phần Auth
public class UserInfoResponse {
    private UUID id;
    private String userName;
    private String email;
    private Set<String> roles;
}

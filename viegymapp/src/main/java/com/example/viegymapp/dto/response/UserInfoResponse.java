package com.example.viegymapp.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private UUID id;
    private String userName;
    private String email;
    private List<String> roles;
}

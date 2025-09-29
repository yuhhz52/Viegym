package com.example.viegymapp.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String accessToken;
	private String type = "Bearer";
	private String refreshToken;
	private UUID id;
	private String userName;
	private String email;
	private List<String> roles;
    
}

package com.example.viegymapp.dto.response;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String accessToken;
	@Builder.Default
	private String tokenType = "Bearer";
	private String refreshToken;
	private UUID id;
	private String userName;
	private String email;
	private Set<String> roles;
    
}

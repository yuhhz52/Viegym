package com.example.viegymapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    @NotBlank
    @Size(min = 5, message = "USERNAME_INVALID")
    private String userName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;
}

package com.example.viegymapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL) // các trường null sẽ bị ẩn
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String userName;
    private String email;
    private String fullName;
    private String gender;
    private LocalDate birthDate;
    private Integer heightCm;
    private Double weightKg;
    private Double bodyFatPercent;
    private String experienceLevel;
    private String goal;
    private Boolean isActive;
    private Set<String> roles;

}

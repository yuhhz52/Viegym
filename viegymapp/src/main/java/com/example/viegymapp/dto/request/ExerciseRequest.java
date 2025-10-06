package com.example.viegymapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String muscleGroup;

    private String difficulty; // easy, medium, hard

    private Set<String> tags; // tên tag

    private String metadata; // JSON string

}
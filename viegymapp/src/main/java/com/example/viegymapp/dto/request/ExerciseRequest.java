package com.example.viegymapp.dto.request;

import com.example.viegymapp.dto.response.UserResponse;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseRequest {

    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private String muscleGroup;
    private String difficulty; // easy, medium, hard
    private Set<String> tags; // tên tag
    private JsonNode metadata; // JSON string
    private UUID createdById;


}
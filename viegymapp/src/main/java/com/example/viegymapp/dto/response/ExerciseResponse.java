package com.example.viegymapp.dto.response;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseResponse {
    private UUID id;
    private String name;
    private String description;
    private String muscleGroup;
    private String difficulty;
    private Set<String> tags;
    private String metadata;
}

package com.example.viegymapp.dto.request;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseUpdateRequest {

    private String name;
    private String description;
    private String muscleGroup;
    private String difficulty;
    private Set<String> tags;
    private String metadata;
}
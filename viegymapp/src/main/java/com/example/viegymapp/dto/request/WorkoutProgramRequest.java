package com.example.viegymapp.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutProgramRequest {
    private String title;
    private String description;
    private String goal;
    private Integer durationWeeks;
    private String visibility; // public/private

}

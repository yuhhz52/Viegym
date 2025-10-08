package com.example.viegymapp.dto.response;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionExerciseLogResponse {
    private UUID id;
    private UUID sessionId;
    private UUID exerciseId;
    private Integer setNumber;
    private Integer repsDone;
    private Double weightUsed;
}

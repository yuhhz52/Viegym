package com.example.viegymapp.dto.request;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionExerciseLogRequest {
    private UUID sessionId;
    private UUID exerciseId;
    private Integer setNumber;
    private Integer repsDone;
    private Double weightUsed;
}

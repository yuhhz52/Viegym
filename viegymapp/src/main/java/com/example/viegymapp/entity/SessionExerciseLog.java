package com.example.viegymapp.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import com.example.viegymapp.entity.BaseEntity.BaseLogEntity;
import java.util.UUID;

@Entity
@Table(name = "session_exercise_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class SessionExerciseLog extends BaseLogEntity{
     @Id
    @UuidGenerator
    @Column(name = "log_id", columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private WorkoutSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "set_number")
    private Integer setNumber;

    @Column(name = "reps_done")
    private Integer repsDone;

    @Column(name = "weight_used")
    private Double weightUsed;
}

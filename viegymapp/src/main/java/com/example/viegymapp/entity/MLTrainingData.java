package com.example.viegymapp.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import com.example.viegymapp.entity.BaseEntity.BaseEntity;
import java.util.UUID;

@Entity
@Table(name = "ml_training_data")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class MLTrainingData extends BaseEntity{
    @Id
    @UuidGenerator
    @Column(name = "ml_id", columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private WorkoutProgram program;

    private Integer feedbackScore;

    @Column(columnDefinition = "jsonb")
    private String features;

    @Column(name = "model_version")
    private String modelVersion;
    
}


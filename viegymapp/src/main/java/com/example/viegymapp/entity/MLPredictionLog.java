package com.example.viegymapp.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import com.example.viegymapp.entity.BaseEntity.BaseLogEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ml_prediction_logs")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class MLPredictionLog extends BaseLogEntity{
    @Id
    @UuidGenerator
    @Column(name = "pred_id", columnDefinition = "UUID")
    private UUID id;

    @ManyToOne 
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "jsonb")
    private String input;

    @Column(columnDefinition = "jsonb")
    private String output;

    @Column(name = "model_version")
    private String modelVersion;
    
}

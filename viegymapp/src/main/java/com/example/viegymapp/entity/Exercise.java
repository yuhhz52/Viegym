package com.example.viegymapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import com.example.viegymapp.entity.BaseEntity.BaseEntity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "exercises", indexes = {
        @Index(name = "idx_exercises_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise extends BaseEntity{
       @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "exercise_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "muscle_group")
    private String muscleGroup;

    private String difficulty; // easy, medium, hard

    @Column(columnDefinition = "TEXT[]")
    private String[] tags;

    @Column(columnDefinition = "JSONB")
    private String metadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseMedia> mediaList;

    @OneToMany(mappedBy = "exercise")
    private Set<ProgramExercise> programExercises = new HashSet<>();

    @OneToMany(mappedBy = "exercise")
    private Set<SessionExerciseLog> sessionLogs = new HashSet<>();
}

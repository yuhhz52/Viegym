package com.example.viegymapp.entity;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.example.viegymapp.entity.BaseEntity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;

import jakarta.persistence.Table;
import lombok.*;
@Entity

@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User extends BaseEntity{
    @Id
    @UuidGenerator
    @Column(name = "user_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    private String nickname;
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "height_cm")
    private Integer heightCm;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "body_fat_percent")
    private Double bodyFatPercent;

    @Column(name = "experience_level")
    private String experienceLevel;

    private String goal;
    private Boolean isActive = true;

    // User roles
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();

    // Exercises
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Exercise> exercises = new HashSet<>();

    // WorkoutProgram
    @OneToMany(mappedBy = "creator")
    private Set<WorkoutProgram> workoutPrograms = new HashSet<>();

    // Sessions
    @OneToMany(mappedBy = "user")
    private Set<WorkoutSession> sessions = new HashSet<>();

    // Nutrition logs
    @OneToMany(mappedBy = "user")
    private Set<NutritionLog> nutritionLogs = new HashSet<>();

    // Health logs
    @OneToMany(mappedBy = "user")
    private Set<HealthLog> healthLogs = new HashSet<>();

    // Community posts
    @OneToMany(mappedBy = "user")
    private Set<CommunityPost> posts = new HashSet<>();

    // Comments
    @OneToMany(mappedBy = "user")
    private Set<PostComment> comments = new HashSet<>();

    // Likes
    @OneToMany(mappedBy = "user")
    private Set<PostLike> likes = new HashSet<>();

    // Refresh tokens
    @OneToMany(mappedBy = "user")
    private Set<RefreshToken> refreshTokens = new HashSet<>();

}

package com.example.viegymapp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.example.viegymapp.entity.BaseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "user_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

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

    // Quan hệ với các entity khác (giữ nguyên code cũ của bạn)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Exercise> exercises = new HashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<WorkoutProgram> workoutPrograms = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<WorkoutSession> sessions = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<NutritionLog> nutritionLogs = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<HealthLog> healthLogs = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<CommunityPost> posts = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<PostComment> comments = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<PostLike> likes = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefreshToken> refreshTokens;


}

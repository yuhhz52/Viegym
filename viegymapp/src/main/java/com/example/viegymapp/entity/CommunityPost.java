package com.example.viegymapp.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import com.example.viegymapp.entity.BaseEntity.BaseEntity;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "community_posts")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class CommunityPost extends BaseEntity{
    @Id
    @UuidGenerator
    @Column(name = "post_id", columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "tsvector")
    private String contentTsvector;

    private String status = "pending";

    @OneToMany(mappedBy = "post")
    private Set<PostMedia> media = new HashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<PostComment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<PostLike> likes = new HashSet<>();
}

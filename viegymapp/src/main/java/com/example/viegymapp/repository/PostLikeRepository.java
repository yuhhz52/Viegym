package com.example.viegymapp.repository;

import com.example.viegymapp.entity.CommunityPost;
import com.example.viegymapp.entity.PostLike;
import com.example.viegymapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {

    Optional<PostLike> findByPostAndUser(CommunityPost post, User user);
}

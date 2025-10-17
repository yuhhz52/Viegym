package com.example.viegymapp.repository;

import com.example.viegymapp.entity.CommunityPost;
import com.example.viegymapp.entity.PostComment;
import com.example.viegymapp.entity.PostLike;
import com.example.viegymapp.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, UUID> {}


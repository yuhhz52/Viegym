package com.example.viegymapp.service;

import com.example.viegymapp.dto.request.CommunityPostRequest;
import com.example.viegymapp.dto.request.PostCommentRequest;
import com.example.viegymapp.dto.response.CommunityPostResponse;
import com.example.viegymapp.dto.response.PostCommentResponse;
import com.example.viegymapp.dto.response.PostLikeResponse;

import java.util.List;
import java.util.UUID;

public interface CommunityService {
    List<CommunityPostResponse> getAllPosts();
    CommunityPostResponse getPostById(UUID id);
    CommunityPostResponse createPost(CommunityPostRequest request);
    CommunityPostResponse updatePost(UUID id, CommunityPostRequest request);
    void deletePost(UUID id);

    // Comments
    PostCommentResponse addComment(UUID postId, PostCommentRequest request);
    List<PostCommentResponse> getCommentsByPost(UUID postId);
    PostCommentResponse updateComment(UUID commentId, PostCommentRequest request);
    void deleteComment(UUID commentId);

    // Likes
    PostLikeResponse likePost(UUID postId);
    void unlikePost(UUID postId);

}

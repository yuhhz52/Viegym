package com.example.viegymapp.controller;

import com.example.viegymapp.dto.request.CommunityPostRequest;
import com.example.viegymapp.dto.request.PostCommentRequest;
import com.example.viegymapp.dto.response.ApiResponse;
import com.example.viegymapp.dto.response.CommunityPostResponse;
import com.example.viegymapp.dto.response.PostCommentResponse;
import com.example.viegymapp.dto.response.PostLikeResponse;
import com.example.viegymapp.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;


    // Posts
    @GetMapping("/posts")
    public ApiResponse<List<CommunityPostResponse>> getAllPosts() {
        return ApiResponse.<List<CommunityPostResponse>>builder()
                .result(communityService.getAllPosts())
                .build();
    }

    @GetMapping("/posts/{id}")
    public ApiResponse<CommunityPostResponse> getPostById(@PathVariable UUID id) {
        return ApiResponse.<CommunityPostResponse>builder()
                .result(communityService.getPostById(id))
                .build();
    }

    @PostMapping("/posts")
    public ApiResponse<CommunityPostResponse> createPost(@RequestBody CommunityPostRequest request) {
        return ApiResponse.<CommunityPostResponse>builder()
                .result(communityService.createPost(request))
                .build();
    }

    @PutMapping("/posts/{id}")
    public ApiResponse<CommunityPostResponse> updatePost(@PathVariable UUID id,
                                                         @RequestBody CommunityPostRequest request) {
        return ApiResponse.<CommunityPostResponse>builder()
                .result(communityService.updatePost(id, request))
                .build();
    }

    @DeleteMapping("/posts/{id}")
    public ApiResponse<UUID> deletePost(@PathVariable UUID id) {
        communityService.deletePost(id);
        return ApiResponse.<UUID>builder()
                .result(id)
                .build();
    }

    // Comments
    @PostMapping("/posts/{id}/comments")
    public ApiResponse<PostCommentResponse> addComment(@PathVariable UUID id,
                                                       @RequestBody PostCommentRequest request) {
        return ApiResponse.<PostCommentResponse>builder()
                .result(communityService.addComment(id, request))
                .build();
    }

    @GetMapping("/posts/{id}/comments")
    public ApiResponse<List<PostCommentResponse>> getComments(@PathVariable UUID id) {
        return ApiResponse.<List<PostCommentResponse>>builder()
                .result(communityService.getCommentsByPost(id))
                .build();
    }

    @PutMapping("/comments/{id}")
    public ApiResponse<PostCommentResponse> updateComment(@PathVariable UUID id,
                                                          @RequestBody PostCommentRequest request) {
        return ApiResponse.<PostCommentResponse>builder()
                .result(communityService.updateComment(id, request))
                .build();
    }

    @DeleteMapping("/comments/{id}")
    public ApiResponse<UUID> deleteComment(@PathVariable UUID id) {
        communityService.deleteComment(id);
        return ApiResponse.<UUID>builder()
                .result(id)
                .build();
    }

    // Likes
    @PostMapping("/posts/{id}/likes")
    public ApiResponse<PostLikeResponse> likePost(@PathVariable UUID id) {
        return ApiResponse.<PostLikeResponse>builder()
                .result(communityService.likePost(id))
                .build();
    }

    @DeleteMapping("/posts/{id}/likes")
    public ApiResponse<UUID> unlikePost(@PathVariable UUID id) {
        communityService.unlikePost(id);
        return ApiResponse.<UUID>builder()
                .result(id)
                .build();
    }

}

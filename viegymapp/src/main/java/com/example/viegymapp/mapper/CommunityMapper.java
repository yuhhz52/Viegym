package com.example.viegymapp.mapper;

import com.example.viegymapp.dto.request.*;
import com.example.viegymapp.dto.response.*;
import com.example.viegymapp.entity.*;
import org.mapstruct.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommunityMapper {

    // -------------------------------
    // CommunityPost
    // -------------------------------
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "media", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "likes", ignore = true)
    CommunityPost toEntity(CommunityPostRequest request);

    @Mapping(target = "authorName", expression = "java(post.getUser() != null && post.getUser().getFullName() != null && !post.getUser().getFullName().isBlank() ? post.getUser().getFullName() : (post.getUser() != null ? post.getUser().getUserName() : null))")
    @Mapping(target = "mediaUrls", expression = "java(mapMediaUrls(post.getMedia()))")
    @Mapping(target = "likeCount", expression = "java((long) post.getLikes().size())")
    @Mapping(target = "commentCount", expression = "java((long) post.getComments().size())")
    @Mapping(target = "createdAt", expression = "java(map(post.getCreatedAt()))")
    CommunityPostResponse toResponse(CommunityPost post);

    default List<String> mapMediaUrls(Set<PostMedia> media) {
        if (media == null || media.isEmpty()) return List.of();
        return media.stream().map(PostMedia::getUrl).collect(Collectors.toList());
    }

    // -------------------------------
    // PostComment
    // -------------------------------
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "replies", ignore = true)
    PostComment toEntity(PostCommentRequest request);

    @Mapping(target = "authorName", expression = "java(comment.getUser() != null && comment.getUser().getFullName() != null && !comment.getUser().getFullName().isBlank() ? comment.getUser().getFullName() : (comment.getUser() != null ? comment.getUser().getUserName() : null))")
    @Mapping(target = "replies", expression = "java(mapReplies(comment.getReplies()))")
    @Mapping(target = "createdAt", expression = "java(map(comment.getCreatedAt()))")
    PostCommentResponse toResponse(PostComment comment);

    default List<PostCommentResponse> mapReplies(Set<PostComment> replies) {
        if (replies == null || replies.isEmpty()) return List.of();
        return replies.stream()
                .map(reply -> {
                    PostCommentResponse r = toResponse(reply);
                    r.setReplies(List.of()); // tránh vòng lặp vô hạn
                    return r;
                })
                .collect(Collectors.toList());
    }

    // -------------------------------
    // PostLike
    // -------------------------------
    default PostLikeResponse toResponse(PostLike like) {
        if (like == null) return null;
        return PostLikeResponse.builder()
                .postId(like.getPost().getId())
                .userId(like.getUser().getId())
                .build();
    }

    // -------------------------------
    // OffsetDateTime -> Instant mapping
    // -------------------------------
    default Instant map(OffsetDateTime value) {
        return value != null ? value.toInstant() : null;
    }
}

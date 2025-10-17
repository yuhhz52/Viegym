package com.example.viegymapp.service.Impl;

import com.example.viegymapp.dto.request.CommunityPostRequest;
import com.example.viegymapp.dto.request.PostCommentRequest;
import com.example.viegymapp.dto.response.CommunityPostResponse;
import com.example.viegymapp.dto.response.PostCommentResponse;
import com.example.viegymapp.dto.response.PostLikeResponse;
import com.example.viegymapp.entity.*;
import com.example.viegymapp.exception.AppException;
import com.example.viegymapp.exception.ErrorCode;
import com.example.viegymapp.mapper.CommunityMapper;
import com.example.viegymapp.repository.*;
import com.example.viegymapp.service.CommunityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityServiceImpl implements CommunityService {
    private final CommunityPostRepository postRepo;
    private final PostCommentRepository commentRepo;
    private final PostLikeRepository likeRepo;
    private final PostMediaRepository mediaRepo;
    private final UserRepository userRepo;
    private final CommunityMapper mapper;

    @Override
    public List<CommunityPostResponse> getAllPosts() {
        return postRepo.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CommunityPostResponse getPostById(UUID id) {
        CommunityPost post = postRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        return mapper.toResponse(post);
    }

    @Override
    public CommunityPostResponse createPost(CommunityPostRequest request) {
        CommunityPost post = mapper.toEntity(request);

        // Lấy current user
        User user = getCurrentUser();
        post.setUser(user);

        // Set status mặc định nếu null
        if (post.getStatus() == null) {
            post.setStatus("pending");
        }

        // Lưu post (flush để Hibernate cập nhật createdAt)
        post = postRepo.saveAndFlush(post);

        if (request.getMediaUrls() != null && !request.getMediaUrls().isEmpty()) {
            for (String url : request.getMediaUrls()) {
                PostMedia media = PostMedia.builder()
                        .post(post)
                        .url(url)
                        .mediaType(detectMediaType(url))
                        .build();
                post.getMedia().add(media);
            }
        }
        post = postRepo.saveAndFlush(post);


        // Ép Hibernate load user để mapper lấy được authorName
        post.setUser(user);

        return mapper.toResponse(post);
    }

    // Hàm phụ: phân loại loại media
    private String detectMediaType(String url) {
        if (url.endsWith(".mp4") || url.endsWith(".mov")) {
            return "VIDEO";
        }
        return "IMAGE";
    }



    @Override
    public CommunityPostResponse updatePost(UUID id, CommunityPostRequest request) {
        CommunityPost post = postRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        // Xóa toàn bộ media cũ trong danh sách
        post.getMedia().clear();

        if (request.getMediaUrls() != null && !request.getMediaUrls().isEmpty()) {
            for (String url : request.getMediaUrls()) {
                PostMedia media = PostMedia.builder()
                        .url(url)
                        .mediaType("IMAGE")
                        .post(post)
                        .build();
                post.getMedia().add(media);
            }
        }

        // Hibernate tự xử lý cascade và orphanRemoval
        postRepo.save(post);

        return mapper.toResponse(post);
    }



    @Override
    public void deletePost(UUID id) {
        CommunityPost post = postRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        mediaRepo.deleteAll(post.getMedia());
        commentRepo.deleteAll(post.getComments());
        likeRepo.deleteAll(post.getLikes());
        postRepo.delete(post);
    }

    @Override
    public PostCommentResponse addComment(UUID postId, PostCommentRequest request) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        PostComment comment = mapper.toEntity(request);
        comment.setPost(post);

        // Lấy current user
        User user = getCurrentUser();
        comment.setUser(user);

        if (request.getParentCommentId() != null) {
            PostComment parent = commentRepo.findById(request.getParentCommentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
            comment.setParentComment(parent);
        }

        // saveAndFlush to ensure createdAt is set before mapping
        comment = commentRepo.saveAndFlush(comment);
        return mapper.toResponse(comment);
    }

    @Override
    public List<PostCommentResponse> getCommentsByPost(UUID postId) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        return post.getComments().stream()
                .filter(c -> c.getParentComment() == null) // chỉ lấy comment gốc
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PostCommentResponse updateComment(UUID commentId, PostCommentRequest request) {
        PostComment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new  AppException(ErrorCode.COMMENT_NOT_FOUND));
        comment.setContent(request.getContent());
        return mapper.toResponse(commentRepo.save(comment));
    }

    @Override
    public void deleteComment(UUID commentId) {
        PostComment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        commentRepo.delete(comment);
    }

    @Override
    public PostLikeResponse likePost(UUID postId) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        User user = getCurrentUser(); // từ SecurityContext

        Optional<PostLike> existing = likeRepo.findByPostAndUser(post, user);
        if (existing.isPresent()) return mapper.toResponse(existing.get());

        PostLike like = PostLike.builder()
                .post(post)
                .user(user)
                .build();
        like = likeRepo.save(like);
        return mapper.toResponse(like);
    }

    @Override
    public void unlikePost(UUID postId) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        User user = getCurrentUser();

        Optional<PostLike> existing = likeRepo.findByPostAndUser(post, user);
        existing.ifPresent(likeRepo::delete);
    }


    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}

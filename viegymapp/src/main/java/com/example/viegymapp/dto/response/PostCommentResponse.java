package com.example.viegymapp.dto.response;

import lombok.*;
import java.time.Instant;
import java.util.UUID;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentResponse {
    private UUID id;
    private String content;
    private String authorName;
    private Instant createdAt;
    private List<PostCommentResponse> replies;
}

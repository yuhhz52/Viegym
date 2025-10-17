package com.example.viegymapp.dto.response;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLikeResponse {
    private UUID postId;
    private UUID userId;
}

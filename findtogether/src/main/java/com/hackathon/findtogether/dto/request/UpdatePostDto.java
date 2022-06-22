package com.hackathon.findtogether.dto.request;

import com.hackathon.findtogether.domain.ResolvingStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdatePostDto {

    private String username;
    private String title;
    private String content;
    private String photo;
    private LocalDateTime updatedAt;
    private String hashtag;
    private ResolvingStatus resolvingStatus;
    private boolean isAnonymous;

}

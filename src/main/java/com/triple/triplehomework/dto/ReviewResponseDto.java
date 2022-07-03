package com.triple.triplehomework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseDto {

    private String reviewId;
    private String content;
    private String userId;
    private String placeId;
    private List<String> attachedPhotoIds;
    private String removeYn;
    private LocalDateTime regDate;
    private LocalDateTime updDate;

}

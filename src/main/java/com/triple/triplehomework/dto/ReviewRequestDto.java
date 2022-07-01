package com.triple.triplehomework.dto;

import com.triple.triplehomework.common.code.ReviewActionCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {

    private String type;
    private ReviewActionCode action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds = Collections.emptyList();
    private String userId;
    private String placeId;
}

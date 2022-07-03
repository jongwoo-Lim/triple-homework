package com.triple.triplehomework.dto;

import com.triple.triplehomework.common.code.ReviewActionCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {

    @NotBlank
    private String type;

    @NotNull
    private ReviewActionCode action;

    private String reviewId;

    @NotBlank
    private String content;

    private List<String> attachedPhotoIds = Collections.emptyList();

    @NotBlank
    private String userId;

    @NotBlank
    private String placeId;

    private String removePhotoYn; // Y: 사진만 삭제 N: 리뷰삭제
}

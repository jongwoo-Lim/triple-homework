package com.triple.triplehomework.dto;

import com.triple.triplehomework.common.code.ReviewActionCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "여행자 리뷰 요청 정보")
public class ReviewRequestDto {

    @NotBlank
    @ApiModelProperty(value = "타입", example = "REVIEW", required = true)
    private String type;

    @NotNull
    @ApiModelProperty(value = "이벤트 타입", example = "ADD", required = true)
    private ReviewActionCode action;

    @ApiModelProperty(value = "리뷰아이디")
    private String reviewId;

    @ApiModelProperty(value = "리뷰내용", example = "좋아요!")
    private String content;

    @ApiModelProperty(value = "첨부파일 아이디", example = "[\"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8\", \"afb0cef2-851d-4a50-bb07-9cc15cbdc332\"]")
    private List<String> attachedPhotoIds = Collections.emptyList();

    @NotBlank
    @ApiModelProperty(value = "회원아이디", example = "3ede0ef2-92b7-4817-a5f3-0c575361f745", required = true)
    private String userId;

    @NotBlank
    @ApiModelProperty(value = "장소아이디", example = "2e4baf1c-5acb-4efb-a1af-eddada31b00f", required = true)
    private String placeId;

    @ApiModelProperty(value = "첨부파일만 삭제 여부")
    private String removePhotoYn; // Y: 사진만 삭제 N: 리뷰삭제
}
